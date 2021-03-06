package br.com.wppatend.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.wpprequest.model.PessoaFisica;

@Service
public class PessoaFisicaRestClient {
	
	@Autowired
	private ParametroService parametroService;
	
	private RestTemplate template = new RestTemplate();
		
	
	public PessoaFisica getPessoaFisicaByTelefoneWA(String fone) {		
		return template.getForObject(parametroService.getApiUrlPessoaFisica() + "/" + fone, PessoaFisica.class);
	}
	
	public PessoaFisica getPessoaFisicaById(Long id) {		
		return template.getForObject(parametroService.getApiUrlPessoaFisica() + "/id/" + id, PessoaFisica.class);
	}
	
	public PessoaFisica savePessoaFisica(PessoaFisica pessoaFisica) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
		HttpEntity<PessoaFisica> entity = new HttpEntity<PessoaFisica>(pessoaFisica, headers);
		
		return template.postForObject(parametroService.getApiUrlPessoaFisica() + "/save" , entity, PessoaFisica.class);
		
	}
	
	public boolean isCpfValido(String cpf) {
		CPFValidator validator = new CPFValidator();
		return validator.invalidMessagesFor(cpf).isEmpty();
	}
	

}
