package br.com.wppatend.flow;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.constraints.DirecaoMensagem;
import br.com.wppatend.entities.Chat;
import br.com.wppatend.entities.FilaAtendimento;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.flow.actions.Action;
import br.com.wppatend.flow.actions.Collector;
import br.com.wppatend.flow.actions.Decision;
import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.entities.FlowInstanceParameter;
import br.com.wppatend.flow.entities.FlowInstancePhoneNumber;
import br.com.wppatend.flow.entities.FlowNode;
import br.com.wppatend.flow.entities.FlowNodeAction;
import br.com.wppatend.flow.entities.FlowNodeCollect;
import br.com.wppatend.flow.entities.FlowNodeDecision;
import br.com.wppatend.flow.entities.FlowNodeEnqueue;
import br.com.wppatend.flow.entities.FlowNodeMenu;
import br.com.wppatend.flow.entities.FlowNodeMenuOption;
import br.com.wppatend.flow.entities.FlowParameter;
import br.com.wppatend.flow.exceptions.ActionException;
import br.com.wppatend.flow.exceptions.CollectorException;
import br.com.wppatend.flow.exceptions.DecisionException;
import br.com.wppatend.flow.services.FlowService;
import br.com.wppatend.services.ChatService;
import br.com.wppatend.services.FilaAtendimentoService;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.services.ProtocoloService;
import br.com.wppatend.wpprequest.model.WppObjectRequest;

@Service
public class FlowEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(FlowEngine.class);

	@Autowired
	private FlowService flowService;
	@Autowired
	private MegaBotRestClient megaBotRestClient;
	@Autowired
	private ParametroService parametroService;
	@Autowired
	private ProtocoloService protocoloService;
	@Autowired
	private FilaAtendimentoService filaAtendimentoService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private PessoaFisicaRestClient pessoaFisicaRestClient;
	@Autowired
	private PessoaJuridicaRestClient pessoaJuridicaRestClient;
	
	public void handleMessage(WppObjectRequest wppObjectRequest) {
		
		String phoneAuthor = StringUtils.split(wppObjectRequest.getMessages().get(0).getAuthor(), "@")[0];
		String body = wppObjectRequest.getMessages().get(0).getBody();
		logger.info(String.format("Processando nova requisição -> phoneAuthor[%1$s]", phoneAuthor));
		
		boolean devMode = parametroService.isModoDesenvolvimento();
		if(devMode) {
			logger.info("Requisição será tratada em developed mode");
		}
		
		List<String> fones = parametroService.getTelefonesDevMode();
		
		logger.info("Autor da mensagem: " + phoneAuthor);
		
		if((devMode && fones.contains(phoneAuthor)) || !devMode) {
			Optional<FlowInstancePhoneNumber> instance = flowService.findFlowInstancePhoneNumberByPhoneNumber(phoneAuthor);
			
			if(instance.isPresent()) {
				/*
				 * Encontrou vinculo de instancia de flow com o numero de telefone
				 */
				FlowInstance inst = instance.get().getFlowInstance();  
				if(inst.getFinishDate() == null) {
					processRequest(inst, phoneAuthor, body, instance.get());
				} else {
					Chat chat = new Chat();
					chat.setBody(body);
					chat.setData_tx_rx(new Date());
					chat.setProtocolo(inst.getProtocolo().getId());
					chat.setLegenda(wppObjectRequest.getMessages().get(0).getCaption());
					chat.setTipo(wppObjectRequest.getMessages().get(0).getType());
					chat.setTx_rx(DirecaoMensagem.RECEBIDA);
					chatService.save(chat);
				}
			} else {
				Flow f = flowService.loadActiveFlow();
				if(f == null) {
					throw new RuntimeException("Do not found any active flow");
				} else {
					
					Date dataInicio = new Date();
					
					Protocolo p = new Protocolo();
					p.setDataInicio(dataInicio);
					p.setFone(phoneAuthor);
					p = protocoloService.save(p);
					
					p.setProtocolo(new SimpleDateFormat("yyyyMMdd").format(p.getDataInicio()) + p.getId());
					p = protocoloService.save(p);
					
					FlowInstance fInst = new FlowInstance();
					List<FlowParameter> parameters = flowService.loadParametersByFlow(f.getId());
					
					if(parameters != null) {
						fInst.setParameters(new ArrayList<>());
						for(FlowParameter fp :  parameters) {
							FlowInstanceParameter fpInst = new FlowInstanceParameter();
							/*fpInst.setClassType(fp.getClassType());
							fpInst.setName(fp.getName());*/
							fpInst.setParameter(fp);
							if(fInst.getParameters() == null) {
								fInst.setParameters(new ArrayList<>());
							}
							fInst.getParameters().add(fpInst);
						}
					}
						
					fInst.setFlow(f);
					fInst.setInitialDate(dataInicio);
					fInst.setProtocolo(p);
					for(FlowNode fn : flowService.loadNodeByFlow(f.getId())) {
						if(fn instanceof FlowNodeAction) {
							if(((FlowNodeAction) fn).isInit()) {
								fInst.setActualNode(fn);
								break;
							}
						}
					}
					
					
					fInst = flowService.saveFlowInstance(fInst);
					
					FlowInstancePhoneNumber fipn = new FlowInstancePhoneNumber();
					fipn.setPhoneNumber(phoneAuthor);
					fipn.setFlowInstance(fInst);
					
					fipn = flowService.saveFlowInstancePhoneNumber(fipn);
					
					processRequest(fInst, phoneAuthor, body, fipn);
						
				}
			}
		} else {
			megaBotRestClient.sendMessage(phoneAuthor, parametroService.getMsgDevMode());
		}
		
	}
	
	private void processRequest(FlowInstance intance, String phoneAuthor, String body, FlowInstancePhoneNumber fipn) {
		
		String msg = processFlowInstanceActualNode(intance, phoneAuthor, body); 
		
		while(msg == null) {
			msg = processFlowInstanceActualNode(intance, phoneAuthor, body);
		}
		
		if(msg.contains("nreco")) {
			throw new RuntimeException(msg);
		}
		
		if(!msg.equals("OK") && !msg.equals("ENQUEUED")) {
			sendMessageToClient(phoneAuthor, msg);
		} else {
			if(!msg.equals("ENQUEUED")) {
				intance.setFinishDate(new Date());
				flowService.saveFlowInstance(intance);
				flowService.deleteFlowIntancePhoneNumber(fipn);
			}
				
		}
	}
	
	private String processFlowInstanceActualNode(FlowInstance instance, String phoneAuthor, String body) {
		
		/*
		 * Action
		 */
		if(instance.getActualNode() instanceof FlowNodeAction) {
			FlowNodeAction nodeAction = (FlowNodeAction) instance.getActualNode();
			try {
				
				Class<?> classe = Class.forName(nodeAction.getActionClass());
				Constructor<?> cons = classe.getConstructor(
						FlowInstance.class, 
						MegaBotRestClient.class,
						ParametroService.class,
						ProtocoloService.class, 
						PessoaFisicaRestClient.class,
						PessoaJuridicaRestClient.class,
						FlowService.class);
				Action a = 
						(Action) cons.newInstance(
								instance, 
								megaBotRestClient, 
								parametroService, 
								protocoloService, 
								pessoaFisicaRestClient, 
								pessoaJuridicaRestClient,
								flowService);
				a.init();
				try {
					a.doAction();
					instance.setActualNode(nodeAction.getOnSuccessNode());
				} catch (ActionException e) {
					instance.setActualNode(nodeAction.getOnErrorNode());
				}
				
				
			} catch (ClassNotFoundException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeAction.getOnErrorNode());
			} catch (NoSuchMethodException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeAction.getOnErrorNode());
			} catch (InvocationTargetException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeAction.getOnErrorNode());
			} catch (IllegalAccessException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeAction.getOnErrorNode());
			} catch (InstantiationException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeAction.getOnErrorNode());
			}
			instance = flowService.saveFlowInstance(instance);
			if(nodeAction.isEnd()) {
				return "OK";
			} else {
				return processReturnMessage(instance.getActualNode());
			}
			
		}
		
		/*
		 * Collect
		 */
		if(instance.getActualNode() instanceof FlowNodeCollect) {
			FlowNodeCollect nodeCollect = (FlowNodeCollect) instance.getActualNode();
			try {
				
				Class<?> classe = Class.forName(nodeCollect.getCollectorClass());
				Constructor<?> cons = classe.getConstructor(FlowInstance.class, 
						MegaBotRestClient.class, ParametroService.class, 
						ProtocoloService.class, PessoaFisicaRestClient.class, 
						PessoaJuridicaRestClient.class, FlowService.class);
				Collector c = (Collector) cons.newInstance(
						instance, megaBotRestClient, parametroService, protocoloService,
						pessoaFisicaRestClient, pessoaJuridicaRestClient, flowService);
				c.doCollect(body);
				instance.setActualNode(nodeCollect.getNextNode());
				
			} catch (ClassNotFoundException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeCollect.getOnErrorNode());
			} catch (NoSuchMethodException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeCollect.getOnErrorNode());
			} catch (InvocationTargetException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeCollect.getOnErrorNode());
			} catch (IllegalAccessException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeCollect.getOnErrorNode());
			} catch (InstantiationException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeCollect.getOnErrorNode());
			} catch (CollectorException e ) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeCollect.getOnErrorNode());
			}
			instance = flowService.saveFlowInstance(instance);
			return processReturnMessage(instance.getActualNode());
		}
		
		/*
		 * Decision
		 */
		if(instance.getActualNode() instanceof FlowNodeDecision) {
			FlowNodeDecision nodeDecision = (FlowNodeDecision) instance.getActualNode();
			try {
				logger.info("Decision class: " + nodeDecision.getDecisionClass());
				Class<?> classe = Class.forName(nodeDecision.getDecisionClass());
				Constructor<?> cons =
						classe.getConstructor(
								FlowInstance.class, 
								MegaBotRestClient.class,
								ParametroService.class,
								ProtocoloService.class,
								PessoaFisicaRestClient.class,
								PessoaJuridicaRestClient.class,
								FlowService.class);
				Decision d = 
						(Decision) cons.newInstance(
								instance, megaBotRestClient, parametroService, protocoloService,
								pessoaFisicaRestClient, pessoaJuridicaRestClient, flowService);
				d.init();
				try {
					if(d.isConditionSatisfied()) {
						instance.setActualNode(nodeDecision.getOnTrueNode());
					} else {
						instance.setActualNode(nodeDecision.getOnFalseNode());
					}
				} catch (DecisionException e) {
					instance.setActualNode(nodeDecision.getOnErrorNode());
				}
				
			} catch (ClassNotFoundException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeDecision.getOnErrorNode());
			} catch (NoSuchMethodException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeDecision.getOnErrorNode());
			} catch (InvocationTargetException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeDecision.getOnErrorNode());
			} catch (IllegalAccessException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeDecision.getOnErrorNode());
			} catch (InstantiationException e) {
				logger.error("An error ocurred during the attempted to execution of this action", e);
				instance.setActualNode(nodeDecision.getOnErrorNode());
			}
			instance = flowService.saveFlowInstance(instance);
			return processReturnMessage(instance.getActualNode());
		}
		
		/*
		 * Enqueue
		 */
		if(instance.getActualNode() instanceof FlowNodeEnqueue) {
			FilaAtendimento fila = new FilaAtendimento();
			fila.setDataFila(new Date());
			fila.setDepartamento(((FlowNodeEnqueue)instance.getActualNode()).getDepartamento());
			fila.setProtocolo(instance.getProtocolo());
			filaAtendimentoService.save(fila);
			instance.setFinishDate(new Date());
			instance = flowService.saveFlowInstance(instance);
			return "ENQUEUED";
		}
		
		/*
		 * Menu
		 */
		if(instance.getActualNode() instanceof FlowNodeMenu) {
			FlowNodeMenu nodeMenu = (FlowNodeMenu) instance.getActualNode();
			List<FlowNodeMenuOption> options = flowService.loadMenuOptionByNodeId(nodeMenu.getId());
			FlowNodeMenuOption findedOption = null;
			for(FlowNodeMenuOption option : options) {
				if(body.toUpperCase().contains(option.getPattern().toUpperCase())) {
					findedOption = option;
					break;
				}
			}
			if(findedOption == null) {
				instance.setActualNode(nodeMenu.getUnrecognizedOptionNode());
			} else {
				instance.setActualNode(findedOption.getNextNode());
			}
			
			instance = flowService.saveFlowInstance(instance);
			return processReturnMessage(instance.getActualNode());
		}
		
		return "Unrecognized node";
		
	}
	
	private void sendMessageToClient(String phoneNumber, String body) {
		megaBotRestClient.sendMessage(phoneNumber, body);
	}
	
	private String processReturnMessage(FlowNode node) {
		if(node instanceof FlowNodeAction) {			
			return null;
		}
		
		if(node instanceof FlowNodeCollect) {
			return ((FlowNodeCollect) node).getMessage();
		}
		
		if(node instanceof FlowNodeDecision) {
			return null;
		}
		
		if(node instanceof FlowNodeEnqueue) {
			return ((FlowNodeEnqueue) node).getMessage();
		}
		
		if(node instanceof FlowNodeMenu) {
			return ((FlowNodeMenu) node).getMessage();
		}
		
		return null;
	}
	
}
