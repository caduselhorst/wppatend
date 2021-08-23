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

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.entities.EstadoAtendimento;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.repositories.ChatRepository;
import br.com.wppatend.repositories.EmpresaContatoRepository;
import br.com.wppatend.repositories.EstadoAtendimentoRespository;
import br.com.wppatend.repositories.FilaAtendimentoRepository;
import br.com.wppatend.repositories.ProtocoloRepository;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.wpprequest.model.WppObjectRequest;

@RestController
public class CustomizableHookController {

	private static final Logger logger = LoggerFactory.getLogger(CustomizableHookController.class);
	
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
	
	@RequestMapping(value = "/web/cust/hook", method = RequestMethod.POST)
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
					// tratando mensagem
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
						logger.info("[" + phoneAuthor + "] Protocolo não localizado. Iniciando novo protocolo.");
						
						megaBotApi.sendMessage(phoneAuthor, parametroService.getMensagemCliente());
						
						Protocolo newProtocolo = new Protocolo();
						newProtocolo.setDataInicio(new Date());
						//p.setEstado(ea);
						newProtocolo.setFone(phoneAuthor);
						
						p = protocoloRepository.save(newProtocolo);
						
						p.setProtocolo(new SimpleDateFormat("yyyyMMdd").format(p.getDataInicio()) + p.getId());
						p = protocoloRepository.save(p);
						
						EstadoAtendimento ea = new EstadoAtendimento();
						ea.setId(1);
						ea.setMensagem("Escolha a opção desejada.\n\n1 - Atendimento\n2 - Promoção Giovanna Baby");
						p.setEstado(ea);
						p = protocoloRepository.save(p);
						
					}
					
					
					
				} else {
					logger.info("Enviando mensagem de developement mode");
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
