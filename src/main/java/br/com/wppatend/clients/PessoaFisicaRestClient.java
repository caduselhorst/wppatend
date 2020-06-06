package br.com.wppatend.clients;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import br.com.wppatend.wpprequest.model.PessoaFisica;

public class PessoaFisicaRestClient {
	
	private static final String HARD_API = "http://localhost:8082/pessoafisica";
	
	private String urlApi = null;
	private RestTemplate template;
	
	public PessoaFisicaRestClient() {
		template = new RestTemplate();
	}
	
	public PessoaFisicaRestClient(String urlApi) {
		this.urlApi = urlApi;
		template = new RestTemplate();
	}
	
	
	public PessoaFisica getPessoaFisicaByTelefoneWA(String fone) {
		if(urlApi == null) {
			return template.getForObject(HARD_API + "/" + fone, PessoaFisica.class);
		} else {
			return template.getForObject(urlApi + "/" + fone, PessoaFisica.class);
		}
		
	}
	
	public PessoaFisica savePessoaFisica(PessoaFisica pessoaFisica) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
		HttpEntity<PessoaFisica> entity = new HttpEntity<PessoaFisica>(pessoaFisica, headers);
		
		if(urlApi == null) {
			return template.postForObject(HARD_API + "/save", entity, PessoaFisica.class);
		} else {
			return template.postForObject(urlApi + "/save" , entity, PessoaFisica.class);
		}
		
	}

}
