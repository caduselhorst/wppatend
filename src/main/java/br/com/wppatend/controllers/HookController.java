package br.com.wppatend.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.lgss.libs.consultaendereco.BOConsultaEndereco;
import br.com.lgss.libs.consultaendereco.ConsultaEnderecoException;
import br.com.lgss.libs.consultaendereco.EnderecoCorreio;
import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.constraints.DirecaoMensagem;
import br.com.wppatend.entities.Chat;
import br.com.wppatend.entities.EmpresaContato;
import br.com.wppatend.entities.EstadoAtendimento;
import br.com.wppatend.entities.EstadoAtendimentoDirecionamento;
import br.com.wppatend.entities.FilaAtendimento;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.flow.FlowEngine;
import br.com.wppatend.repositories.ChatRepository;
import br.com.wppatend.repositories.EmpresaContatoRepository;
import br.com.wppatend.repositories.EstadoAtendimentoRespository;
import br.com.wppatend.repositories.FilaAtendimentoRepository;
import br.com.wppatend.repositories.ProtocoloRepository;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.wpprequest.model.PessoaFisica;
import br.com.wppatend.wpprequest.model.PessoaJuridica;
import br.com.wppatend.wpprequest.model.WppObjectRequest;

@RestController
public class HookController {
	
	private static final Logger logger = LoggerFactory.getLogger(HookController.class);
		
	@Autowired
	private MegaBotRestClient megaBotApi;
	@Autowired
	private PessoaFisicaRestClient pessoaFisicaRestClient;
	@Autowired
	private PessoaJuridicaRestClient pessoaJuridicaRestClient;
	@Autowired
	private ParametroService parametroService;
	@Autowired
	private ProtocoloRepository protocoloRepository;
	@Autowired
	private EstadoAtendimentoRespository estadoAtendimentoRepository;
	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private FilaAtendimentoRepository filaRepository;
	@Autowired
	private EmpresaContatoRepository empresaContatoRepository;
	@Autowired
	private FlowEngine flowEngine;

	@Deprecated
	@RequestMapping(value = "/web/hook", method = RequestMethod.POST)
	public ResponseEntity<String> hook (@RequestBody WppObjectRequest msg) {
		
		logger.info("Nova requisição -> " + String.format("Mensagem para tratamento: %1$s (%2$s) fromMe: %3$s chatId: %4$s Id: %5$s Server date: %6$s", 
				msg.getMessages().get(0).getSenderName().getName(), 
				msg.getMessages().get(0).getAuthor(), 
				msg.getMessages().get(0).getFromMe(),
				msg.getMessages().get(0).getChatId(),
				msg.getMessages().get(0).getId(),
				new Date()));
		
		try {
			if(msg.getMessages().get(0).getFromMe() != null && !msg.getMessages().get(0).getFromMe()) {
				logger.info("Mensagem tratável");
				
				boolean devMode = parametroService.isModoDesenvolvimento();
				if(devMode) {
					logger.info("Requisição será tratada em developed mode");
				}
				
				List<String> fones = parametroService.getTelefonesDevMode();
				
				String phoneAuthor = StringUtils.split(msg.getMessages().get(0).getAuthor(), "@")[0];
				logger.info("Autor da mensagem: " + phoneAuthor);
				
				if((devMode && fones.contains(phoneAuthor)) || !devMode) {
					
					
					String fone = trataTelefone(phoneAuthor);
					Protocolo p = null;
					try {
						logger.info("[" + phoneAuthor + "] Recuperando protocolo");
						p = protocoloRepository.findProtocoloAbertoByFone(phoneAuthor);
					} catch (Exception e) {
						logger.error("[" + phoneAuthor + "] Erro ao recuperar o protocolo", e);
						megaBotApi.sendMessage(phoneAuthor, parametroService.getMensagemErro());
						return ResponseEntity.ok("Ok");
					}
					
					if(p == null) {
						
						//if(parametroService.isHorarioAtendimento()) {
						logger.info("[" + phoneAuthor + "] Protocolo não localizado. Iniciando novo protocolo.");
						
						megaBotApi.sendMessage(phoneAuthor, parametroService.getMensagemCliente());
						
						Protocolo newProtocolo = new Protocolo();
						newProtocolo.setDataInicio(new Date());
						//p.setEstado(ea);
						newProtocolo.setFone(phoneAuthor);
						
						p = protocoloRepository.save(newProtocolo);
						
						p.setProtocolo(new SimpleDateFormat("yyyyMMdd").format(p.getDataInicio()) + p.getId());
						p = protocoloRepository.save(p);
						logger.info("[" + phoneAuthor + "] Protocolo criado. Recuperando cliente");
						PessoaFisica pf = null;
						
						if(parametroService.isTrataCliente()) {
							try {
								pf = pessoaFisicaRestClient.getPessoaFisicaByTelefoneWA(phoneAuthor);
							} catch (Exception e) {
								logger.error("[" + phoneAuthor + "] Erro ao recuperar o cliente", e);
								megaBotApi.sendMessage(phoneAuthor, parametroService.getMensagemErro());
								return ResponseEntity.ok("Ok");
							}
							
							if(pf == null) {
								logger.info("[" + phoneAuthor + "] Cliente não localizado. Iniciando processo de cadastro");
								EstadoAtendimento ea = estadoAtendimentoRepository.findById(Integer.parseInt("1")).get();
								p.setEstado(ea);
								p = protocoloRepository.save(p);
								logger.info("[" + phoneAuthor + "] Estado do atendimento atualizado -> [" + ea.getId() + "]");
								logger.info("[" + phoneAuthor + "] Criando registro inicial do cliente");
								pf = new PessoaFisica();
								pf.setNumerowa(phoneAuthor);
								pf.setNumerocel(fone);
								pf.setDataCadastro(new Date());
								
								try {
									pf = pessoaFisicaRestClient.savePessoaFisica(pf);
									logger.info("[" + phoneAuthor + "] Registro criado. Id[" + pf.getIdpessoaf() + "]");
								} catch (Exception e) {
									logger.error("[" + phoneAuthor + "] Erro ao criar o cliente", e);
									megaBotApi.sendMessage(phoneAuthor, parametroService.getMensagemErro());
									return ResponseEntity.ok("Ok");
								}
								
								p.setCodPessoa(pf.getIdpessoaf());
								p = protocoloRepository.save(p);
								logger.info("[" + phoneAuthor + "] Protocolo atualizado. Mandando mensagem ao cliente");
								megaBotApi.sendMessage(phoneAuthor, ea.getMensagem());
								logger.info("[" + phoneAuthor + "] Mensagem enviada.");
								
							} else {
								logger.info("[" + phoneAuthor + "] Cliente localizado. Id:[" + pf.getIdpessoaf() + "] Enviando mensagem de cumprimento.");
								megaBotApi.sendMessage(phoneAuthor, "Olá " + pf.getNome());
								logger.info("[" + phoneAuthor + "] Cliente localizado. Id:[" + pf.getIdpessoaf() + "] Mensagem enviada. Setando estado do atendimento.");
								EstadoAtendimento ea = estadoAtendimentoRepository.findById(Integer.parseInt("10")).get();
								p.setEstado(ea);
								p.setCodPessoa(pf.getIdpessoaf());
								p = protocoloRepository.save(p);
								logger.info("[" + phoneAuthor + "] Cliente localizado. Id:[" + pf.getIdpessoaf() + "] Ok! Enviando menu ao cliente");
								megaBotApi.sendMessage(phoneAuthor, ea.getMensagem());
								logger.info("[" + phoneAuthor + "] Cliente localizado. Id:[" + pf.getIdpessoaf() + "] Mensagem enviada");
							}
							//} else {
							//	logger.info("[" + phoneAuthor + "] Requisição fora do expediente. Enviado mensagem ao cliente");
							//	megaBotApi.sendMessage(phoneAuthor, parametroService.getMensagemHorarioAtendimento());
							//	logger.info("[" + phoneAuthor + "] Ok");
							//}
						} else {
							logger.info("Aplicação configurada para não tratar o cliente.");
							logger.info("Setando estado de chat no protocolo");
							EstadoAtendimento ea = estadoAtendimentoRepository.findById(11).get();
							p.setEstado(ea);
							p = protocoloRepository.save(p);
							logger.info("Ok. Gravando mensagem do cliente como chat.");
							Chat chat = new Chat();
							chat.setBody(msg.getMessages().get(0).getBody());
							chat.setTipo("chat");
							chat.setData_tx_rx(new Date());
							chat.setTx_rx(DirecaoMensagem.RECEBIDA);
							chat.setProtocolo(p.getId());
							chatRepository.save(chat);
							logger.info("Ok. Enfileirando atendimento");
							FilaAtendimento fila = new FilaAtendimento();
							fila.setDataFila(new Date());
							fila.setProtocolo(p);
							filaRepository.save(fila);
							logger.info("OK");
						}
						
					} else {
						
						EstadoAtendimento ea = p.getEstado();
						logger.info("[" + phoneAuthor + "] Protocolo recuperado. Estado atual: [" + ea.getId() + "] Iniciando processamento.");
						switch(ea.getId()) {
							case 1: {
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								logger.info("[" + phoneAuthor + "] Mensagem digitada pelo cliente: [" + msg.getMessages().get(0).getBody().toUpperCase() + "].");
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase().replace(".", "").replace(" ", ""))) {
										logger.info("[" + phoneAuthor + "] Mensagem entendida. Alterado estado do protocolo e enviado mensagem ao cliente.");
										EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
										
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
										logger.info("[" + phoneAuthor + "] Ok.");
									}
								}
								break;
							}
							case 2: {
								logger.info("[" + phoneAuthor + "] Alterado estado do protocolo e enviado mensagem ao cliente.");
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								
								EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
								p.setEstado(nEa);
								
								protocoloRepository.save(p);
								megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem().replace("@nome", p.getUltMsgDig()));
								logger.info("[" + phoneAuthor + "] Ok.");
								break;
							}
							case 3: {
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
										logger.info("[" + phoneAuthor + "] Mensagem entendida.");
										if(ead.getConfirmacao()) {
											logger.info("[" + phoneAuthor + "] Atualizando registro do cliente.");
											PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
											pf.setNome(p.getUltMsgDig().toUpperCase());
											pf = pessoaFisicaRestClient.savePessoaFisica(pf);
											logger.info("[" + phoneAuthor + "] Registro atualizado.");
										}
										logger.info("[" + phoneAuthor + "] Alterando estado do protocolo e enviando mensagem ao cliente.");
										EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
										logger.info("[" + phoneAuthor + "] Ok.");
									}
								}
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								break;
							}
							case 4: {
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								logger.info("[" + phoneAuthor + "] Alterando protocolo e enviando mensagem ao cliente.");
								p = protocoloRepository.save(p);
								EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
								p.setEstado(nEa);
								
								p.setDataAlteracao(new Date());
								p.setEstado(nEa);
								
								p = protocoloRepository.save(p);
								
								megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem().replace("@cpf", p.getUltMsgDig()));
								logger.info("[" + phoneAuthor + "] Ok.");
								break;
							}
							case 5: {
								boolean achou = false;
								EstadoAtendimento nEa = null;
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
										logger.info("[" + phoneAuthor + "] Mensagem entendida.");
										if(ead.getConfirmacao()) {
											String cpf = p.getUltMsgDig();
											logger.info("[" + phoneAuthor + "] Validando cpf.");
											if(pessoaFisicaRestClient.isCpfValido(cpf)) {
												logger.info("[" + phoneAuthor + "] CPF Ok. Atualizando dados do cliente.");
												try {
													PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
													pf.setCpf(cpf);
													pessoaFisicaRestClient.savePessoaFisica(pf);
													logger.info("[" + phoneAuthor + "] Registro atualizado. Recuperando proximo estado");
												} catch (Exception e) {
													logger.error("[" + phoneAuthor + "] Ocorreu um erro ao atualizar os dados do cliente.", e);
													megaBotApi.sendMessage(phoneAuthor, parametroService.getMensagemErro());
													return ResponseEntity.ok("OK");
												}
												
												nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
												logger.info("[" + phoneAuthor + "] Estado recuperado.");
											} else {
												logger.info("[" + phoneAuthor + "] CPF inválido. Informando o cliente.");
												megaBotApi.sendMessage(phoneAuthor, "Tem algo errado com número. Pode repetir, por favor?");
												nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstadoErro()).get();
												//megaBotApi.sendMessage(phoneAuthor, ea.getMensagem().replace("@cpf", p.getUltMsgDig()));
												logger.info("[" + phoneAuthor + "] Ok.");
											}
											logger.info("[" + phoneAuthor + "] Atualizando estado do protocolo e enviando a mensagem para o cliente.");
											p.setEstado(nEa);
											p.setDataAlteracao(new Date());
											p = protocoloRepository.save(p);
											megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
											logger.info("[" + phoneAuthor + "] Ok.");
										} else {
											logger.info("[" + phoneAuthor + "] Atualizando estado do protocolo e enviando a mensagem para o cliente.");
											nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
											megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
											p.setEstado(nEa);
											p.setDataAlteracao(new Date());
											p = protocoloRepository.save(p);
											logger.info("[" + phoneAuthor + "] Ok.");
										}
										achou = true;
									}
								}
								if(!achou) {
									megaBotApi.sendMessage(phoneAuthor, "Ops! Algo deu errado. Vamos tentar novamente?\n" + p.getEstado().getMensagem());
								}
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								break;
							}
							case 6: {
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								String cep = msg.getMessages().get(0).getBody();
								
								BOConsultaEndereco bo = new BOConsultaEndereco();
								try {
									logger.info("[" + phoneAuthor + "] Buscando dados de endereço no WS.");
									EnderecoCorreio ec = bo.buscaEnderecoPeloCEP(cep);
									logger.info("[" + phoneAuthor + "] Ok. Buscando  próximo estado do protocolo.");
									EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
									
									String nmsg = nEa.getMensagem().replace("@uf", ec.getUf())
											.replace("@localidade", ec.getLocalidade())
											.replace("@bairro", ec.getBairro())
											.replace("@logradouro", ec.getLogradouro());
									logger.info("[" + phoneAuthor + "] Ok. Atualizando os dados do cliente, setando protocolo e enviando mensagem ao cliente.");
									PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
									pf.setCep(cep);
									pf.setCidade(ec.getLocalidade().toUpperCase());
									pf.setLogradouro(ec.getLogradouro().toUpperCase());
									pf.setBairro(ec.getBairro().toUpperCase());
									pf.setEstado(ec.getUf());
									pf = pessoaFisicaRestClient.savePessoaFisica(pf);
									
									p.setEstado(nEa);
									p = protocoloRepository.save(p);
									
									megaBotApi.sendMessage(phoneAuthor, nmsg);
									logger.info("[" + phoneAuthor + "] Ok.");
									
								} catch (ConsultaEnderecoException e) {
									logger.error("[" + phoneAuthor + "] Erro ao buscar o endereço.", e);
									megaBotApi.sendMessage(phoneAuthor, "Ops! Algo deu errado na consulta do endereço. Você digita o cep novamente?");
									return ResponseEntity.ok("Ok");
								}
								break;
							}
							case 7: {
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								boolean achou = false;
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
										logger.info("[" + phoneAuthor + "] Mensagem entendida. Atualizando protocolo e enviando mensagem ao cliente");
										EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
										logger.info("[" + phoneAuthor + "] Ok.");
										achou = true;
									}
								}
								if(!achou) {
									logger.info("[" + phoneAuthor + "] Resposta não identificada. Solicitando ao cliente uma nova tentativa");
									megaBotApi.sendMessage(phoneAuthor, "Ops! Algo deu errado. Vamos tentar novamente?\n" + p.getEstado().getMensagem());
									logger.info("[" + phoneAuthor + "] Ok.");
								}
								break;
							}
							case 8: {
								logger.info("[" + phoneAuthor + "] Atualizando protocolo e enviando mensagem ao cliente.");
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
								p.setEstado(nEa);
								
								p.setDataAlteracao(new Date());
								p.setEstado(nEa);
								
								p = protocoloRepository.save(p);
								
								megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem().replace("@nro", p.getUltMsgDig()));
								logger.info("[" + phoneAuthor + "] Ok.");
								
								break;
							}
							case 9: {
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								if(!msg.getMessages().get(0).getBody().toUpperCase().contains("NÃO") && !msg.getMessages().get(0).getBody().toUpperCase().contains("NAO")) {
									try {
										logger.info("[" + phoneAuthor + "] Atualizando dados do cliente.");
										PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
										pf.setComplemento(msg.getMessages().get(0).getBody().toUpperCase());
										pf = pessoaFisicaRestClient.savePessoaFisica(pf);
										logger.info("[" + phoneAuthor + "] Ok.");
									} catch (Exception e) {
										logger.error("[" + phoneAuthor + "] Ocorreu um erro ao atualizar os dados do cliente.", e);
										return ResponseEntity.ok("OK");
									}
								}
								logger.info("[" + phoneAuthor + "] Atualizando estado do protocolo e enviando mensagem ao cliente.");
								EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
								p.setEstado(nEa);
								
								protocoloRepository.save(p);
								megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
								logger.info("[" + phoneAuthor + "] Ok.");
								break;
							}
							case 10: {
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								boolean achou = false;
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(msg.getMessages().get(0).getBody().toUpperCase().contains(ead.getResposta())) {
										logger.info("[" + phoneAuthor + "] Mensagem reconhecida. Atualizado protocolo");
										EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										p.setEstado(nEa);
										logger.info("[" + phoneAuthor + "] Ok.");
										protocoloRepository.save(p);
										if(ead.getConfirmacao()) {
											if(parametroService.isHorarioAtendimento()) {
												logger.info("[" + phoneAuthor + "] Direcionando protocolo para fila de atendimento.");
												FilaAtendimento fila = new FilaAtendimento();
												fila.setDataFila(new Date());
												fila.setProtocolo(p);
												filaRepository.save(fila);
												//logger.info("[" + phoneAuthor + "] Direcionando protocolo para fila de atendimento.");
												//logger.info("[" + phoneAuthor + "] Enviando mensagem ao cliente.");
												//megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem().replace("@protocolo", p.getProtocolo()));
												logger.info("[" + phoneAuthor + "] Ok.");
											} else {
												logger.info("[" + phoneAuthor + "] Requisição fora do horário de atendimento.");
												logger.info("[" + phoneAuthor + "] Encerrando protocolo e enviado mensagem ao cliente.");
												p.setDataFechamento(new Date());
												p = protocoloRepository.save(p);
												megaBotApi.sendMessage(phoneAuthor, parametroService.getMensagemHorarioAtendimento());
												logger.info("[" + phoneAuthor + "] Ok.");
											}
												
											achou = true;
										}
										
										
									}
								}
								if(!achou) {
									megaBotApi.sendMessage(phoneAuthor, "Ops! Algo deu errado. Vamos tentar novamente?\n" + p.getEstado().getMensagem());
								}
								break;
							}
							case 11: {
								try {
									logger.info("[" + phoneAuthor + "] Gravando mensagem (chat) do cliente.");
									Chat chat = new Chat();
									chat.setTipo(msg.getMessages().get(0).getType());
									chat.setBody(msg.getMessages().get(0).getBody());
									chat.setLegenda(msg.getMessages().get(0).getCaption());
									chat.setProtocolo(p.getId());
									chat.setTx_rx(DirecaoMensagem.RECEBIDA);
									chat.setData_tx_rx(new Date());
									chatRepository.save(chat);
									logger.info("[" + phoneAuthor + "] Ok.");
								} catch (Exception e) {
									logger.error("[" + phoneAuthor + "] Ocorreu um erro ao gravar a mensagem do cliente.", e);
								}
								break;
							}
							case 12: {
								logger.info("[" + phoneAuthor + "] Atualizando protocolo.");
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								p.setDataFechamento(new Date());
								p = protocoloRepository.save(p);
								logger.info("[" + phoneAuthor + "] Ok.");
								break;
							}
							case 13: {
								logger.info("[" + phoneAuthor + "] Atualizando protocolo e enviando mensagem ao cliente.");
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								
								
								EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
								p.setEstado(nEa);
								
								protocoloRepository.save(p);
								megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem().replace("@email", p.getUltMsgDig()));
								logger.info("[" + phoneAuthor + "] Ok.");
								break;
							}
							case 14: {
								
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
										logger.info("[" + phoneAuthor + "] Mensagem reconhecida.");
										if(ead.getConfirmacao()) {
											logger.info("[" + phoneAuthor + "] Atualizando dados do cliente.");
											try {
												PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
												pf.setEmail(p.getUltMsgDig().toLowerCase());
												pf = pessoaFisicaRestClient.savePessoaFisica(pf);
												logger.info("[" + phoneAuthor + "] Ok.");
											} catch (Exception e) {
												logger.error("[" + phoneAuthor + "] Ocorreu um erro ao atualizar os dados do cliente.", e);
											}
										}
										logger.info("[" + phoneAuthor + "] Atualizando dados do protocolo e enviando mensagem ao cliente.");
										EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
										megaBotApi.sendMessage(phoneAuthor, "Obrigado pelas informações!");
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
										logger.info("[" + phoneAuthor + "] Ok.");
									}
								}
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								break;
							}
							case 15: {
								logger.info("[" + phoneAuthor + "] Atualizando protocolo e enviando mensagem ao cliente.");
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								//p = protocoloRepository.save(p);
								p.setEstado(estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get());
								p.setDataAlteracao(new Date());
								p = protocoloRepository.save(p);
								megaBotApi.sendMessage(phoneAuthor, p.getEstado().getMensagem().replace("@cnpj", p.getUltMsgDig()));
								logger.info("[" + phoneAuthor + "] Ok.");
								break;
							}
							case 16: {
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
										logger.info("[" + phoneAuthor + "] Mesagem reconhecida.");
										EstadoAtendimento nEa = null;
										String message = "";
										if(ead.getConfirmacao()) {
											logger.info("[" + phoneAuthor + "] Validando CNPJ.");
											if(pessoaJuridicaRestClient.isCnpjValido(p.getUltMsgDig())) {
												logger.info("[" + phoneAuthor + "] CNPJ válido.");
												PessoaJuridica pj = pessoaJuridicaRestClient.getPessoaJuridicaByCNPJ(p.getUltMsgDig());	
												nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
												message = nEa.getMensagem().replace("@razao", pj.getRazaosocial()).replace("@fantasia", pj.getFantasia()).replace("@cnpj", pj.getCnpj());
											} else {
												logger.info("[" + phoneAuthor + "] CNPJ inválido.");
												megaBotApi.sendMessage(phoneAuthor, "Tem algo errado com número. Pode repetir, por favor?");
												nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstadoErro()).get();
											}
											
										} else {
											nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
											message = nEa.getMensagem();
										}
										logger.info("[" + phoneAuthor + "] Atualizando protocolo e enviando mensagem ao cliente.");
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
										megaBotApi.sendMessage(phoneAuthor, message);
										logger.info("[" + phoneAuthor + "] Ok.");
									}
								}
								p = protocoloRepository.save(p);
								break;
							}
							case 17: {
								
								boolean achou = false;
								EstadoAtendimento nEa = null;
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
										logger.info("[" + phoneAuthor + "] Resposta conhecida.");
										if(ead.getConfirmacao()) {
											logger.info("[" + phoneAuthor + "] Recuperando cliente e vinculando contato.");
											PessoaJuridica pj = pessoaJuridicaRestClient.getPessoaJuridicaByCNPJ(p.getUltMsgDig());
											p.setCodPessoaJuridica(pj.getIdpessoaj());
											p = protocoloRepository.save(p);
											List<EmpresaContato> ecs = empresaContatoRepository.findBypessoaf(p.getCodPessoa());
											EmpresaContato ec = new EmpresaContato();
											ec.setPessoaf(p.getCodPessoa());
											ec.setPessoaj(p.getCodPessoaJuridica());
											if(!ecs.contains(ec)) {
												empresaContatoRepository.save(ec);
											}
											logger.info("[" + phoneAuthor + "] Ok. Enviando protocolo para a fila");
											FilaAtendimento fila = new FilaAtendimento();
											fila.setDataFila(new Date());
											fila.setProtocolo(p);
											filaRepository.save(fila);
											logger.info("[" + phoneAuthor + "] Ok.");
										}
										
										logger.info("[" + phoneAuthor + "] Atualizando protocolo e enviando mensagem ao cliente.");
										nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
										logger.info("[" + phoneAuthor + "] Ok.");
										achou = true;
									}
									
								}
								
								if(!achou) {
									logger.info("[" + phoneAuthor + "] Resposta não conhecida. Enviando mensagem ao cliente");
									megaBotApi.sendMessage(phoneAuthor, "Ops! Algo deu errado. Vamos tentar novamente?\n" + p.getEstado().getMensagem());
									logger.info("[" + phoneAuthor + "] Ok.");
								}
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								break;
							}
							case 18: {
								
								boolean achou = false;
								EstadoAtendimento nEa = null;
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
										logger.info("[" + phoneAuthor + "] Reposta reconhecida.");
										if(ead.getConfirmacao()) {
											logger.info("[" + phoneAuthor + "] Atualizando dados do cliente, atualizando estado do protocolo e enviando mensagem ao cliente.");
											String nro = p.getUltMsgDig();
											PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
											pf.setNumero(nro);
											pessoaFisicaRestClient.savePessoaFisica(pf);
											nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
											
											p.setEstado(nEa);
											p.setDataAlteracao(new Date());
											p = protocoloRepository.save(p);
											megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
											logger.info("[" + phoneAuthor + "] Ok.");
										} else {
											logger.info("[" + phoneAuthor + "] Atualizando protocolo e enviando mensagem ao cliente.");
											nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
											megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
											p.setEstado(nEa);
											p.setDataAlteracao(new Date());
											p = protocoloRepository.save(p);
											logger.info("[" + phoneAuthor + "] Ok.");
										}
										achou = true;
									}
								}
								if(!achou) {
									logger.info("[" + phoneAuthor + "] Mensagem não reconhecida. Enviando mensagem ao cliente");
									megaBotApi.sendMessage(phoneAuthor, "Ops! Algo deu errado. Vamos tentar novamente?\n" + p.getEstado().getMensagem());
									logger.info("[" + phoneAuthor + "] Ok.");
								}
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								break;
							}
							case 19: {
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								boolean achou = false;
								EstadoAtendimento nEa = null;
								for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
									if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
										logger.info("[" + phoneAuthor + "] Mensagem reconhecida. Atualizando dados do cliente, estado do protocolo e enviando mensagem ao cliente");
										PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
										
										
										if(msg.getMessages().get(0).getBody().toUpperCase().contains("S")) {
											pf.setDesejaReceberOfertas(true);
										} else {
											pf.setDesejaReceberOfertas(false);
										}
										
										pessoaFisicaRestClient.savePessoaFisica(pf);
										nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
										logger.info("[" + phoneAuthor + "] Ok.");
										achou = true;
									}
								}
								if(!achou) {
									logger.info("[" + phoneAuthor + "] Resposta não reconhecida. Envia mensagem ao cliente");
									megaBotApi.sendMessage(phoneAuthor, "Ops! Algo deu errado. Vamos tentar novamente?\n" + p.getEstado().getMensagem());
									logger.info("[" + phoneAuthor + "] Ok.");
								}
								p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
								p = protocoloRepository.save(p);
								break;
							}
						}
					}
					
						
				} else {
					megaBotApi.sendMessage(msg.getMessages().get(0).getAuthor(), parametroService.getMsgDevMode());
				}
			}
		} catch (NullPointerException e) {
			logger.error("Erro durante o processamento", e);
		}
			
			
		return ResponseEntity.ok("Ok");
		
	}
	
	private String trataTelefone(String telefone) {
		if(telefone.length() == 13) {
			return telefone;
		} else {
			return telefone.substring(0,4) + "9" + telefone.substring(4, 12);
		}
	}
	
	@RequestMapping(value = "/web/flow/hook", method = RequestMethod.POST)
	public ResponseEntity<String> flowHook (@RequestBody WppObjectRequest msg) {
		try {
			if(msg.getMessages() != null) {
				logger.info("Nova requisição -> " + String.format("Mensagem para tratamento: %1$s (%2$s) fromMe: %3$s chatId: %4$s Id: %5$s Server date: %6$s", 
						msg.getMessages().get(0).getSenderName().getName(), 
						msg.getMessages().get(0).getAuthor(), 
						msg.getMessages().get(0).getFromMe(),
						msg.getMessages().get(0).getChatId(),
						msg.getMessages().get(0).getId(),
						new Date()));
				
					if(msg.getMessages().get(0).getFromMe() != null && !msg.getMessages().get(0).getFromMe()) {
						flowEngine.handleMessage(msg);
					}
			}
				
		} catch (NullPointerException e) {
			logger.error("Erro durante o processamento", e);
		}
		return ResponseEntity.ok("Ok");
	}
	

}
