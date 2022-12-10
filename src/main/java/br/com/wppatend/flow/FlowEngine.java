package br.com.wppatend.flow;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.constraints.DirecaoMensagem;
import br.com.wppatend.entities.Chat;
import br.com.wppatend.entities.FilaAtendimento;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.entities.User;
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
import br.com.wppatend.flow.entities.FlowNodeMessage;
import br.com.wppatend.flow.entities.FlowParameter;
import br.com.wppatend.flow.exceptions.ActionException;
import br.com.wppatend.flow.exceptions.CollectorException;
import br.com.wppatend.flow.exceptions.DecisionException;
import br.com.wppatend.flow.services.FlowService;
import br.com.wppatend.notificadores.MessageType;
import br.com.wppatend.notificadores.MessageWrapper;
import br.com.wppatend.services.ChatService;
import br.com.wppatend.services.FilaAtendimentoService;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.services.ProtocoloService;
import br.com.wppatend.services.UserService;
import br.com.wppatend.wpprequest.model.Message;
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
	@Autowired
	private UserService userService;
	
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
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
					processRequest(inst, wppObjectRequest.getMessages().get(0), instance.get());
				} else {
					Chat chat = new Chat();
					chat.setBody(body);
					chat.setData_tx_rx(new Date());
					chat.setProtocolo(inst.getProtocolo().getId());
					chat.setLegenda(wppObjectRequest.getMessages().get(0).getCaption());
					chat.setTipo(wppObjectRequest.getMessages().get(0).getType());
					chat.setChatId(wppObjectRequest.getMessages().get(0).getChatId());
					chat.setMessageId(wppObjectRequest.getMessages().get(0).getMessageId());
					chat.setTx_rx(DirecaoMensagem.RECEBIDA);
					chatService.save(chat);
					
					User user = userService.loadById(inst.getProtocolo().getOperador()).get();
					
					MessageWrapper wrapper = MessageWrapper.builder()
							.type(MessageType.CHAT.toString())
							.message(chat)
							.build();
					
					// envia mensagem ao websocket
					simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/messages", wrapper);
				}
			} else {
				
				// trata hora de expediente
				if(parametroService.isHorarioAtendimento() && !parametroService.isFeriado()) {
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
							if(fn instanceof FlowNodeMessage) {
								if(((FlowNodeMessage) fn).isInit()) {
									fInst.setActualNode(fn);
									break;
								}
							}
						}
						
						
						fInst = flowService.saveFlowInstance(fInst);
						
						if(parameters != null) {
	
							for(FlowParameter fp :  parameters) {
								FlowInstanceParameter fpInst = new FlowInstanceParameter();
								fpInst.setFlowInstance(fInst);
								fpInst.setParameter(fp);
								flowService.saveFlowInstanceParameter(fpInst);
							}
						}
							
						
						
						
						FlowInstancePhoneNumber fipn = new FlowInstancePhoneNumber();
						fipn.setPhoneNumber(phoneAuthor);
						fipn.setFlowInstance(fInst);
						
						fipn = flowService.saveFlowInstancePhoneNumber(fipn);
						
						processRequest(fInst, wppObjectRequest.getMessages().get(0), fipn);
							
					}
				} else {
					megaBotRestClient.sendMessage(phoneAuthor, parametroService.getMensagemHorarioAtendimento());
				}
					
			}
		} else {
			megaBotRestClient.sendMessage(phoneAuthor, parametroService.getMsgDevMode());
		}
		
	}
	
	/**
	 * Método que invoa o processamento do node atual, para avaliar se é necessária a criação de protoco, ou se envia mensagens ao destinatário 
	 * 
	 * @param intance
	 * @param message
	 * @param fipn
	 */
	private void processRequest(FlowInstance intance, Message message, FlowInstancePhoneNumber fipn) {
		
		String phoneAuthor = StringUtils.split(message.getAuthor(), "@")[0];
		
		String msg = processFlowInstanceActualNode(intance, message); 
		
		while(msg == null) {
			msg = processFlowInstanceActualNode(intance, message);
		}
		
		if(msg.contains("nreco")) {
			throw new RuntimeException(msg);
		}
		
		if(!msg.equals("OK") && !msg.equals("ENQUEUED")) {
			if(msg.startsWith("MENU")) {
				
				String[] splitedMessage = StringUtils.split(msg, "|");
				
				String m = splitedMessage[1];
				
				List<String> options = Arrays.asList(StringUtils.split(splitedMessage[2], ","));
				
				sendMessageButtonToClient(phoneAuthor, m, options);
				
				
			} else {
				sendMessageToClient(phoneAuthor, msg);
			}
				
		} else {
			if(!msg.equals("ENQUEUED")) {
				intance.setFinishDate(new Date());
				Protocolo p = intance.getProtocolo();
				p.setDataFechamento(new Date());
				p = protocoloService.save(p);
				intance.setProtocolo(p);
				flowService.saveFlowInstance(intance);
				flowService.deleteFlowIntancePhoneNumber(fipn);
			}
				
		}
	}
	
	private String processFlowInstanceActualNode(FlowInstance instance, Message message) {
		
		String phoneAuthor = StringUtils.split(message.getAuthor(), "@")[0]; 
		String body = message.getBody();
		String selectedDisplayText = message.getSelectedDisplayText(); 
		
		FlowNode flowNode = instance.getActualNode();
		
		/*
		 * Action
		 */
		if(flowNode instanceof FlowNodeAction) {
			FlowNodeAction nodeAction = (FlowNodeAction) flowNode;
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
		if(flowNode instanceof FlowNodeCollect) {
			FlowNodeCollect nodeCollect = (FlowNodeCollect) flowNode;
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
		if(flowNode instanceof FlowNodeDecision) {
			FlowNodeDecision nodeDecision = (FlowNodeDecision) flowNode;
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
		if(flowNode instanceof FlowNodeEnqueue) {
			sendMessageToClient(phoneAuthor, ((FlowNodeEnqueue) flowNode).getMessage());
			FilaAtendimento fila = new FilaAtendimento();
			fila.setDataFila(new Date());
			fila.setDepartamento(((FlowNodeEnqueue) flowNode).getDepartamento());
			fila.setProtocolo(instance.getProtocolo());
			filaAtendimentoService.save(fila);
			instance.setFinishDate(new Date());
			instance = flowService.saveFlowInstance(instance);
			return "ENQUEUED";
		}
		
		/*
		 * Menu
		 */
		if(flowNode instanceof FlowNodeMenu) {
			FlowNodeMenu nodeMenu = (FlowNodeMenu) flowNode;
			List<FlowNodeMenuOption> options = flowService.loadMenuOptionByNodeId(nodeMenu.getId());
			FlowNodeMenuOption findedOption = null;
			for(FlowNodeMenuOption option : options) {
				if(option.getPattern().toUpperCase().equals(selectedDisplayText.toUpperCase())) {
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
		
		/*
		 * Message
		 */
		if(flowNode instanceof FlowNodeMessage) {
			FlowNodeMessage node = (FlowNodeMessage) flowNode;
			
			try {
				megaBotRestClient.sendMessage(phoneAuthor, node.getMessage());
				instance.setActualNode(node.getOnSuccessNode());
				instance = flowService.saveFlowInstance(instance);
				if(node.isEnd()) {
					return "OK";
				} else {
					return processReturnMessage(instance.getActualNode());
				}
			} catch (Exception e) {
				instance.setActualNode(node.getOnErrorNode());
				instance = flowService.saveFlowInstance(instance);
				return processReturnMessage(instance.getActualNode());
			}
			
		}
		
		return "Unrecognized node";
		
	}
	
	private void sendMessageToClient(String phoneNumber, String body) {
		megaBotRestClient.sendMessage(phoneNumber, body);
	}
	
	private void sendMessageButtonToClient(String phoneNumber, String message, List<String> options) {
		megaBotRestClient.sendMessageButtons(phoneNumber, message, "Escolha uma opção abaixo", options);
	}
	
	/**
	 * Método privado que identifica o tipo do node e retorna um texto para controle.
	 * Retorna a mensagem a ser enviada ao destinarário, baseado no tipo do node.
	 * Retorno null significa que o node processado não possuir interação com o destinatário do processo e habilita o processamento ao próximo node.
	 * 
	 * @param node
	 * @return
	 */
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
			//return ((FlowNodeEnqueue) node).getMessage();
			return null;
		}
		
		if(node instanceof FlowNodeMenu) {
			/*
			 * Alteração para identificar o menu com botões 
			 */
			FlowNodeMenu menu = (FlowNodeMenu) node;
			
			// Carrega as opções
			List<FlowNodeMenuOption> options = flowService.loadMenuOptionByNodeId(menu.getId());
			
			// gera uma string com o texto dos botões cadastrados nas opções do menu, separados por vírgula
			String opts = options.
							stream()
							.map(o -> o.getPattern())
							.collect(Collectors.joining(","));
			
			// Monta a mensagem no formato: MENU|Informe sobre o que iremos falar|VENDAS,SUPORTE,FINANCEIRO e retorna
			String message = String.format("MENU|%s|%s", ((FlowNodeMenu) node).getMessage(), opts);
			
			return message;
		}
		
		return null;
	}
	
}
