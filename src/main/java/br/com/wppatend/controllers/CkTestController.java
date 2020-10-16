package br.com.wppatend.controllers;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.entities.EstadoAtendimento;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.gixdb.Dao;
import br.com.wppatend.repositories.ChatRepository;
import br.com.wppatend.repositories.EmpresaContatoRepository;
import br.com.wppatend.repositories.EstadoAtendimentoRespository;
import br.com.wppatend.repositories.FilaAtendimentoRepository;
import br.com.wppatend.repositories.ProtocoloRepository;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.vos.ProdutoCK;
import br.com.wppatend.wpprequest.model.PessoaFisica;
import br.com.wppatend.wpprequest.model.WppObjectRequest;

@RestController
public class CkTestController {
	
	private static final Logger logger = LoggerFactory.getLogger(CkTestController.class);
		
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

	@RequestMapping(value = "/web/testhook", method = RequestMethod.POST)
	public ResponseEntity<String> hook (@RequestBody WppObjectRequest msg) {
		
		logger.info("Nova requisição -> " + String.format("Mensagem para tratamento: %1$s (%2$s) fromMe: %3$s chatId: %4$s Id: %5$s Server date: %6$s", 
				msg.getMessages().get(0).getSenderName(), 
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
						newProtocolo.setEstado(estadoAtendimentoRepository.findById(1).get());
						
						p = protocoloRepository.save(newProtocolo);
						
						p.setProtocolo(new SimpleDateFormat("yyyyMMdd").format(p.getDataInicio()) + p.getId());
						p = protocoloRepository.save(p);
						logger.info("[" + phoneAuthor + "] Protocolo criado. Recuperando cliente");
						PessoaFisica pf = null;
						
						megaBotApi.sendMessage(phoneAuthor, p.getEstado().getMensagem());
						
					} else {
						
						EstadoAtendimento ea = p.getEstado();
						logger.info("[" + phoneAuthor + "] Protocolo recuperado. Estado atual: [" + ea.getId() + "] Iniciando processamento.");
						
						try {
							Integer codprod = Integer.parseInt(msg.getMessages().get(0).getBody());
							Dao dao = new Dao();
							ProdutoCK prod = dao.loadProduct(codprod);
							if(prod == null) {
								//
								megaBotApi.sendMessage(phoneAuthor, "Produto não localizado");
							} else {
								//calculos report
								Integer percAvista = 0;
								Integer qtdParcCartao = 10;
								String planoCredCenter = "1+4";
								Integer qtdParcCredCenter = 4;
								Integer qtdParcLosango = 10;
								Double fatorLosango = 0.1493;
								Integer descLosango = 10;
								Integer credCenterPercEntrada = 30;
								Double cartaoExt = prod.getTabpprec() / qtdParcCartao;
								Double credCenter = prod.getTabpprec() / qtdParcCredCenter;
								Double losangoVlrParc = (100-descLosango) * prod.getTabpprec()/100*fatorLosango;
								Double aVista = (100-percAvista)*prod.getTabpprec()/100;
								Double losangoVlrTotal = qtdParcLosango*losangoVlrParc;
								Double entradaCredcenter = (100-credCenterPercEntrada)*prod.getTabpprec()/100;
								
								
								//mensagem cliente
								String msgProd = "*" + prod.getProddesc() + "*\n";
								msgProd += "*À VISTA*\n";
								msgProd += NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(aVista) + "\n";
								msgProd += "*CARTÃO DE CRÉDITO*\n";
								msgProd += "*" + qtdParcCartao + "X* " + NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(cartaoExt) + "\n";
								msgProd += "*CREDCENTER*\n";
								msgProd += "*" + planoCredCenter + "* " + NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(credCenter) + "\n";
								msgProd += "*CREDIÁRIO EXTERNO*\n";
								msgProd += "*" + qtdParcLosango + "X* " + NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(losangoVlrParc) + "\n\n";
								
								megaBotApi.sendMessage(phoneAuthor, msgProd);
								
							}
						} catch (NumberFormatException e) {
							megaBotApi.sendMessage(phoneAuthor, "Informe somente números");
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							megaBotApi.sendMessage(phoneAuthor, "Ocorreu uma falha na tentativa de leitura de dados na base do GIX");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							megaBotApi.sendMessage(phoneAuthor, "Ocorreu uma falha na tentativa de leitura de dados na base do GIX");
						}
						
						megaBotApi.sendMessage(phoneAuthor, p.getEstado().getMensagem());
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

}
