package br.com.wppatend.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import br.com.lgss.libs.consultaendereco.EnderecoCorreio;
import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.constraints.DirecaoMensagem;
import br.com.wppatend.entities.Chat;
import br.com.wppatend.entities.EstadoAtendimento;
import br.com.wppatend.entities.EstadoAtendimentoDirecionamento;
import br.com.wppatend.entities.FilaAtentimento;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.repositories.ChatRepository;
import br.com.wppatend.repositories.EstadoAtendimentoRespository;
import br.com.wppatend.repositories.FilaAtendimentoRepository;
import br.com.wppatend.repositories.ProtocoloRepository;
import br.com.wppatend.services.ConfigurationService;
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
	private ConfigurationService configurationService;
	@Autowired
	private ProtocoloRepository protocoloRepository;
	@Autowired
	private EstadoAtendimentoRespository estadoAtendimentoRepository;
	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private FilaAtendimentoRepository filaRepository;

	@RequestMapping(value = "/web/hook", method = RequestMethod.POST)
	public ResponseEntity<String> hook (@RequestBody WppObjectRequest msg) {
		
		if(!msg.getMessages().get(0).getFromMe()) {
			logger.info(String.format("Mensagem de: %1$s (%2$s) fromMe: %3$s chatId: %4$s Id: %5$s Server date: %6$s", 
					msg.getMessages().get(0).getSenderName(), 
					msg.getMessages().get(0).getAuthor(), 
					msg.getMessages().get(0).getFromMe(),
					msg.getMessages().get(0).getChatId(),
					msg.getMessages().get(0).getId(),
					new Date()));
			boolean devMode = configurationService.isModoDesenvolvimento();
			List<String> fones = configurationService.getTelefonesDevMode();
			
			String phoneAuthor = StringUtils.split(msg.getMessages().get(0).getAuthor(), "@")[0];
			
			if((devMode && fones.contains(phoneAuthor)) || !devMode) {
				
				logger.info("Nova mensagem para tratamento");
				String fone = trataTelefone(phoneAuthor);
				
				Protocolo p = protocoloRepository.findProtocoloAbertoByFone(phoneAuthor);
				
				if(p == null) {
					
					megaBotApi.sendMessage(phoneAuthor, configurationService.getMensagemCliente());
					
					Protocolo newProtocolo = new Protocolo();
					newProtocolo.setDataInicio(new Date());
					//p.setEstado(ea);
					newProtocolo.setFone(phoneAuthor);
					
					p = protocoloRepository.save(newProtocolo);
					
					p.setProtocolo(new SimpleDateFormat("yyyyMMdd").format(p.getDataInicio()) + p.getId());
					p = protocoloRepository.save(p);
					
					PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaByTelefoneWA(phoneAuthor);
					if(pf == null) {
						
						EstadoAtendimento ea = estadoAtendimentoRepository.findById(Integer.parseInt("1")).get();
						p.setEstado(ea);
						p = protocoloRepository.save(p);
						
						pf = new PessoaFisica();
						pf.setNumerowa(phoneAuthor);
						pf.setNumerocel(fone);
						pf.setDataCadastro(new Date());
						
						pf = pessoaFisicaRestClient.savePessoaFisica(pf);
						
						p.setCodPessoa(pf.getIdpessoaf());
						p = protocoloRepository.save(p);
						
						megaBotApi.sendMessage(phoneAuthor, ea.getMensagem());
						
					} else {
						megaBotApi.sendMessage(phoneAuthor, "Olá " + pf.getNome());
						EstadoAtendimento ea = estadoAtendimentoRepository.findById(Integer.parseInt("10")).get();
						p.setEstado(ea);
						p.setCodPessoa(pf.getIdpessoaf());
						p = protocoloRepository.save(p);
						
						megaBotApi.sendMessage(phoneAuthor, ea.getMensagem());
					}
				} else {
					EstadoAtendimento ea = p.getEstado();
					
					switch(ea.getId()) {
						case 1: {
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
								if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase().replace(".", "").replace(" ", ""))) {
									EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
									p.setEstado(nEa);
									p.setDataAlteracao(new Date());
									p = protocoloRepository.save(p);
									megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
								}
							}
							break;
						}
						case 2: {
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							//PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
							//pf.setNome(msg.getMessages().get(0).getBody().toUpperCase());
							//pf = pessoaFisicaRestClient.savePessoaFisica(pf);
							
							EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
							p.setEstado(nEa);
							
							protocoloRepository.save(p);
							megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem().replace("@nome", p.getUltMsgDig()));
							break;
						}
						case 3: {
							for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
								if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
									if(ead.getConfirmacao()) {
										PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
										pf.setNome(p.getUltMsgDig().toUpperCase());
										pf = pessoaFisicaRestClient.savePessoaFisica(pf);
									}
									EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
									p.setEstado(nEa);
									p.setDataAlteracao(new Date());
									p = protocoloRepository.save(p);
									megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
								}
							}
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							break;
						}
						case 4: {
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
							p.setEstado(nEa);
							
							p.setDataAlteracao(new Date());
							p.setEstado(nEa);
							
							p = protocoloRepository.save(p);
							
							megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem().replace("@cpf", p.getUltMsgDig()));
							break;
						}
						case 5: {
							boolean achou = false;
							EstadoAtendimento nEa = null;
							for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
								if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
									if(ead.getConfirmacao()) {
										String cpf = p.getUltMsgDig();
										if(pessoaFisicaRestClient.isCpfValido(cpf)) {
											PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
											pf.setCpf(cpf);
											pessoaFisicaRestClient.savePessoaFisica(pf);
											nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
											
										} else {
											megaBotApi.sendMessage(phoneAuthor, "Tem algo errado com número. Pode repetir, por favor?");
											nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstadoErro()).get();
											//megaBotApi.sendMessage(phoneAuthor, ea.getMensagem().replace("@cpf", p.getUltMsgDig()));
										}
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
									} else {
										nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
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
								EnderecoCorreio ec = bo.buscaEnderecoPeloCEP(cep);
								
								EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
								
								String nmsg = nEa.getMensagem().replace("@uf", ec.getUf())
										.replace("@localidade", ec.getLocalidade())
										.replace("@bairro", ec.getBairro())
										.replace("@logradouro", ec.getLogradouro());
								
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
								
							} catch (Exception e) {
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
									EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
									p.setEstado(nEa);
									p.setDataAlteracao(new Date());
									p = protocoloRepository.save(p);
									megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
									achou = true;
								}
							}
							if(!achou) {
								megaBotApi.sendMessage(phoneAuthor, "Ops! Algo deu errado. Vamos tentar novamente?\n" + p.getEstado().getMensagem());
							}
							break;
						}
						case 8: {
							
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
							p.setEstado(nEa);
							
							p.setDataAlteracao(new Date());
							p.setEstado(nEa);
							
							p = protocoloRepository.save(p);
							
							megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem().replace("@nro", p.getUltMsgDig()));
							
							break;
						}
						case 9: {
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							if(!msg.getMessages().get(0).getBody().toUpperCase().contains("NÃO") && !msg.getMessages().get(0).getBody().toUpperCase().contains("NAO")) {
								PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
								pf.setComplemento(msg.getMessages().get(0).getBody().toUpperCase());
								pf = pessoaFisicaRestClient.savePessoaFisica(pf);
							}
							EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
							p.setEstado(nEa);
							
							protocoloRepository.save(p);
							megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
							break;
						}
						case 10: {
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							boolean achou = false;
							for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
								if(msg.getMessages().get(0).getBody().toUpperCase().contains(ead.getResposta())) {
									EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
									p.setEstado(nEa);
									protocoloRepository.save(p);
									if(ead.getConfirmacao()) {
										FilaAtentimento fila = new FilaAtentimento();
										fila.setDataFila(new Date());
										fila.setProtocolo(p);
										filaRepository.save(fila);
									}
										
									megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
									achou = true;
								}
							}
							if(!achou) {
								megaBotApi.sendMessage(phoneAuthor, "Ops! Algo deu errado. Vamos tentar novamente?\n" + p.getEstado().getMensagem());
							}
							break;
						}
						case 11: {
							Chat chat = new Chat();
							if(msg.getMessages().get(0).getType().equals("chat")) {
								chat.setMsg_texto(msg.getMessages().get(0).getBody());
							}
							chat.setProtocolo(p.getId());
							chat.setTx_rx(DirecaoMensagem.RECEBIDA);
							chat.setData_tx_rx(new Date());
							chatRepository.save(chat);
							break;
						}
						case 12: {
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							p.setDataFechamento(new Date());
							p = protocoloRepository.save(p);
							break;
						}
						case 13: {
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							
							
							EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get();
							p.setEstado(nEa);
							
							protocoloRepository.save(p);
							megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem().replace("@email", p.getUltMsgDig()));
							break;
						}
						case 14: {
							
							for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
								if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
									if(ead.getConfirmacao()) {
										PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
										pf.setEmail(p.getUltMsgDig().toLowerCase());
										pf = pessoaFisicaRestClient.savePessoaFisica(pf);
									}
									EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
									p.setEstado(nEa);
									p.setDataAlteracao(new Date());
									p = protocoloRepository.save(p);
									megaBotApi.sendMessage(phoneAuthor, "Obrigado pelas informações!");
									megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
								}
							}
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							break;
						}
						case 15: {
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							//p = protocoloRepository.save(p);
							p.setEstado(estadoAtendimentoRepository.findById(ea.getDirecionamentos().get(0).getIdProximoEstado()).get());
							p.setDataAlteracao(new Date());
							p = protocoloRepository.save(p);
							megaBotApi.sendMessage(phoneAuthor, p.getEstado().getMensagem().replace("@cnpj", p.getUltMsgDig()));
							break;
						}
						case 16: {
							for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
								if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
									EstadoAtendimento nEa = null;
									String message = "";
									if(ead.getConfirmacao()) {
										if(pessoaJuridicaRestClient.isCnpjValido(p.getUltMsgDig())) {
											PessoaJuridica pj = pessoaJuridicaRestClient.getPessoaJuridicaByCNPJ(p.getUltMsgDig());
											nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
											message = nEa.getMensagem().replace("@razao", pj.getRazaosocial()).replace("@fantasia", pj.getFantasia()).replace("@cnpj", pj.getCnpj());
										} else {
											megaBotApi.sendMessage(phoneAuthor, "Tem algo errado com número. Pode repetir, por favor?");
											nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstadoErro()).get();
										}
										
									} else {
										nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										message = nEa.getMensagem();
									}
									
									p.setEstado(nEa);
									p.setDataAlteracao(new Date());
									p = protocoloRepository.save(p);
									megaBotApi.sendMessage(phoneAuthor, message);
									
								}
							}
							//p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							p = protocoloRepository.save(p);
							break;
						}
						case 17: {
							
							boolean achou = false;
							EstadoAtendimento nEa = null;
							for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
								/*
								EstadoAtendimento nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
								p.setEstado(nEa);
								p.setDataAlteracao(new Date());
								p = protocoloRepository.save(p);
								megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
								*/
								if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
									if(ead.getConfirmacao()) {
										PessoaJuridica pj = pessoaJuridicaRestClient.getPessoaJuridicaByCNPJ(p.getUltMsgDig());
										p.setCodPessoaJuridica(pj.getIdpessoaj());
										p = protocoloRepository.save(p);
										FilaAtentimento fila = new FilaAtentimento();
										fila.setDataFila(new Date());
										fila.setProtocolo(p);
										filaRepository.save(fila);
									}
									
									nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
									p.setEstado(nEa);
									p.setDataAlteracao(new Date());
									//p.setCodPessoaJuridica(pj.getIdpessoaj());
									p = protocoloRepository.save(p);
									megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
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
						case 18: {
							
							boolean achou = false;
							EstadoAtendimento nEa = null;
							for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
								if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
									if(ead.getConfirmacao()) {
										String nro = p.getUltMsgDig();
										PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
										pf.setNumero(nro);
										pessoaFisicaRestClient.savePessoaFisica(pf);
										nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
									} else {
										nEa = estadoAtendimentoRepository.findById(ead.getIdProximoEstado()).get();
										megaBotApi.sendMessage(phoneAuthor, nEa.getMensagem());
										p.setEstado(nEa);
										p.setDataAlteracao(new Date());
										p = protocoloRepository.save(p);
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
						case 19: {
							p.setUltMsgDig(msg.getMessages().get(0).getBody().toUpperCase());
							/*
							p = protocoloRepository.save(p);
							PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
							if(msg.getMessages().get(0).getBody().toUpperCase().contains("S")) {
								pf.setDesejaReceberOfertas(true);
							} else {
								pf.setDesejaReceberOfertas(false);
							}
							pessoaFisicaRestClient.savePessoaFisica(pf);
							*/
							boolean achou = false;
							EstadoAtendimento nEa = null;
							for(EstadoAtendimentoDirecionamento ead : ea.getDirecionamentos()) {
								if(ead.getResposta().contains(msg.getMessages().get(0).getBody().toUpperCase())) {
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
					}
				}
			} else {
				megaBotApi.sendMessage(msg.getMessages().get(0).getAuthor(), configurationService.getMsgDevMode());
			}
			
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

}
