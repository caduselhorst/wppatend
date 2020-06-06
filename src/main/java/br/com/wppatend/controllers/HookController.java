package br.com.wppatend.controllers;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.wpprequest.model.PessoaFisica;
import br.com.wppatend.wpprequest.model.WppObjectRequest;

@RestController
public class HookController {
	
	private static final Logger logger = LoggerFactory.getLogger(HookController.class);
	
	@Value("${api.pessoafisica}")
	private String urlPessoaFisicaApi;
	@Value("${api.pessoajuridica}")
	private String urlPessoaJuridicaApi;
	
	@RequestMapping(value = "/hook", method = RequestMethod.POST)
	public void hook (@RequestBody WppObjectRequest msg) {
		logger.info(String.format("Mensagem de: %1$s (%2$s) fromMe: %3$s chatId: %4$s Id: %5$s", 
				msg.getMessages().get(0).getChatName(), 
				msg.getMessages().get(0).getAuthor(), 
				msg.getMessages().get(0).getFromMe()),
				msg.getMessages().get(0).getChatId(),
				msg.getMessages().get(0).getId());
		
		if(msg.getMessages().get(0).getAuthor().contains("559699720884")) {
			
			logger.info(String.format("Alana me mandando msg. Id: %1$s", msg.getMessages().get(0).getId()));
			
			/*
			logger.info("Instanciando client rest." + (urlPessoaFisicaApi == null ? "" : " URL api pessoa fisica -> " + urlPessoaFisicaApi));
			PessoaFisicaRestClient client = new PessoaFisicaRestClient(urlPessoaFisicaApi);
			
			PessoaFisica pf = client.getPessoaFisicaByTelefoneWA(trataTelefone(StringUtils.split(
					msg.getMessages().get(0).getAuthor(), "@")[0]));
			
			if(pf == null) {
				PessoaFisica npf = new PessoaFisica();
				npf.setBairro("Trem");
				npf.setCidade("Macapá");
				npf.setComplemento("Apto. 3");
				npf.setData_cadastro(new Date());
				npf.setEmail("xxxxx@gmail.com");
				npf.setEstado("AP");
				npf.setLogradouro("Odilardo Silva");
				npf.setNome("Alana Barbosa");
				npf.setNumero("857");
				npf.setNumero_wa(trataTelefone(trataTelefone(StringUtils.split(
					msg.getMessages().get(0).getAuthor(), "@")[0])));
				npf.setTipo_logradouro("R");
				
				client.savePessoaFisica(npf);
			} else {
				logger.info("Pessoa já cadastrada");
			}
			*/
		}
		
	}
	
	private String trataTelefone(String telefone) {
		if(telefone.length() == 13) {
			return telefone;
		} else {
			return telefone.substring(0,4) + "9" + telefone.substring(4, 12);
		}
	}

}
