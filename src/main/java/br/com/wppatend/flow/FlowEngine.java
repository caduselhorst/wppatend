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
import br.com.wppatend.constraints.DirecaoMensagem;
import br.com.wppatend.entities.Chat;
import br.com.wppatend.entities.FilaAtendimento;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.flow.actions.Action;
import br.com.wppatend.flow.actions.Collector;
import br.com.wppatend.flow.actions.Decision;
import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.entities.FlowInstancePhoneNumber;
import br.com.wppatend.flow.entities.FlowNode;
import br.com.wppatend.flow.entities.FlowNodeAction;
import br.com.wppatend.flow.entities.FlowNodeCollect;
import br.com.wppatend.flow.entities.FlowNodeDecision;
import br.com.wppatend.flow.entities.FlowNodeEnqueue;
import br.com.wppatend.flow.entities.FlowNodeMenu;
import br.com.wppatend.flow.entities.FlowNodeMenuOption;
import br.com.wppatend.flow.entities.FlowParameter;
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
					processRequest(inst, phoneAuthor, body);
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
					
					if(f.getParameters() != null) {
						fInst.setParameters(new ArrayList<>());
						for(FlowParameter fp :  f.getParameters()) {
							FlowParameter fpInst = new FlowParameter();
							fpInst.setClassType(fp.getClassType());
							fpInst.setName(fp.getName());
							fInst.getParameters().add(fpInst);
						}
					}
						
					fInst.setFlow(f);
					fInst.setInitialDate(dataInicio);
					fInst.setProtocolo(p);
					fInst.setActualNode(f.getNodes().get(0));
					
					fInst = flowService.saveFlowInstance(fInst);
					
					FlowInstancePhoneNumber fipn = new FlowInstancePhoneNumber();
					fipn.setPhoneNumber(phoneAuthor);
					fipn.setFlowInstance(fInst);
					
					fipn = flowService.saveFlowInstancePhoneNumber(fipn);
					
					processRequest(fInst, phoneAuthor, body);
						
				}
			}
		} else {
			megaBotRestClient.sendMessage(phoneAuthor, parametroService.getMsgDevMode());
		}
		
	}
	
	private void processRequest(FlowInstance intance, String phoneAuthor, String body) {
		
		String msg = processFlowInstanceActualNode(intance, phoneAuthor, body); 
		
		while(msg == null) {
			msg = processFlowInstanceActualNode(intance, phoneAuthor, body);
		}
		
		if(!msg.equals("OK")) {
			sendMessageToClient(phoneAuthor, msg);
		}
	}
	
	private String processFlowInstanceActualNode(FlowInstance instance, String phoneAuthor, String body) {
		
		if(instance.getActualNode() instanceof FlowNodeAction) {
			FlowNodeAction nodeAction = (FlowNodeAction) instance.getActualNode();
			try {
				
				Class<?> classe = Class.forName(nodeAction.getActionClass());
				Constructor<?> cons = classe.getConstructor(FlowInstance.class, 
						MegaBotRestClient.class, ParametroService.class);
				Action a = (Action) cons.newInstance(instance, 
						megaBotRestClient, parametroService);
				a.init();
				a.doAction();
				instance.setActualNode(nodeAction.getOnSuccessNode());
				
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
			return processReturnMessage(instance.getActualNode());
		}
		
		if(instance.getActualNode() instanceof FlowNodeCollect) {
			FlowNodeCollect nodeCollect = (FlowNodeCollect) instance.getActualNode();
			try {
				
				Class<?> classe = Class.forName(nodeCollect.getCollectorClass());
				Constructor<?> cons = classe.getConstructor(FlowInstance.class, 
						MegaBotRestClient.class, ParametroService.class);
				Collector c = (Collector) cons.newInstance(
						instance, megaBotRestClient, parametroService);
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
			}
			instance = flowService.saveFlowInstance(instance);
			return processReturnMessage(instance.getActualNode());
		}
		
		if(instance.getActualNode() instanceof FlowNodeDecision) {
			FlowNodeDecision nodeDecision = (FlowNodeDecision) instance.getActualNode();
			try {
				
				Class<?> classe = Class.forName(nodeDecision.getDecisionClass());
				Constructor<?> cons = classe.getConstructor(FlowInstance.class, 
						MegaBotRestClient.class, ParametroService.class, FlowService.class);
				Decision d = (Decision) cons.newInstance(
						instance, megaBotRestClient, parametroService, flowService);
				d.init();
				if(d.isConditionSatisfied()) {
					instance.setActualNode(nodeDecision.getOnTrueNode());
				} else {
					instance.setActualNode(nodeDecision.getOnFalseNode());
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
		
		if(instance.getActualNode() instanceof FlowNodeEnqueue) {
			FilaAtendimento fila = new FilaAtendimento();
			fila.setDataFila(new Date());
			fila.setDepartamento(((FlowNodeEnqueue)instance.getActualNode()).getDepartamento());
			fila.setProtocolo(instance.getProtocolo());
			filaAtendimentoService.save(fila);
			instance.setFinishDate(new Date());
			instance = flowService.saveFlowInstance(instance);
			return "OK";
		}
		
		if(instance.getActualNode() instanceof FlowNodeMenu) {
			FlowNodeMenu nodeMenu = (FlowNodeMenu) instance.getActualNode();
			FlowNodeMenuOption findedOption = null;
			for(FlowNodeMenuOption option : nodeMenu.getOptions()) {
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
		sendMessageToClient(phoneNumber, body);
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
