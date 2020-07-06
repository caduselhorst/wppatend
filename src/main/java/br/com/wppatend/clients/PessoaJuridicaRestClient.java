
package br.com.wppatend.clients;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.wpprequest.model.ConsultaCNPJResponseObject;
import br.com.wppatend.wpprequest.model.EmpresaAtv;
import br.com.wppatend.wpprequest.model.PessoaJuridica;

@Service
public class PessoaJuridicaRestClient {
	
	@Autowired
	private ParametroService parametroService;
	
	private RestTemplate template = new RestTemplate();
	
	public PessoaJuridica getPessoaJuridicaByCNPJ(String cnpj) {
		PessoaJuridica pj =template.getForObject(parametroService.getApiUrlPessoaJuridica() + "/cnpj/" + cnpj, PessoaJuridica.class);
		if(pj == null) {
			pj = getPessoaJuridicaFromApiConsultaCNPJ(cnpj);
			savePessoaJuridica(pj);
		}
		return pj;
	}
	
	public PessoaJuridica savePessoaJuridica(PessoaJuridica pessoaJuridica) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
		HttpEntity<PessoaJuridica> entity = new HttpEntity<PessoaJuridica>(pessoaJuridica, headers);
		
		return template.postForObject(parametroService.getApiUrlPessoaJuridica() , entity, PessoaJuridica.class);
		
	}
	
	public PessoaJuridica getPessoaJuridicaFromApiConsultaCNPJ(String cnpj) {
		
		ConsultaCNPJResponseObject obj = template.getForObject(parametroService.getUrlApiConsultaCNPJ() + "/" + cnpj, ConsultaCNPJResponseObject.class);
		
		//String resp = template.getForObject(configurationService.getUrlApiConsultaCNPJ() + "/" + cnpj, String.class);
		
		PessoaJuridica pj = new PessoaJuridica();
		
		
		pj.setBairro(obj.getBairro());
		pj.setCep(obj.getCep());
		pj.setCidade(obj.getMunicipio());
		pj.setCnpj(obj.getCnpj().replace("/", "").replace(".", "").replace("-", ""));
		pj.setComplemento(pj.getComplemento());
		pj.setEmail(obj.getEmail());
		pj.setEstado(obj.getUf());
		pj.setFantasia(obj.getFantasia());
		pj.setLogradouro(obj.getLogradouro());
		pj.setNumero(obj.getNumero());
		pj.setRazaosocial(obj.getNome());
		ArrayList<EmpresaAtv> atividades = new ArrayList<>();
		obj.getAtividade_principal().forEach(item -> {
			atividades.add(new EmpresaAtv(item.getCode(), item.getText()));
		});
		obj.getAtividades_secundarias().forEach(item -> {
			atividades.add(new EmpresaAtv(item.getCode(), item.getText()));
		});
		pj.setAtividades(atividades);
		
		return pj;
		
	}
	
	public PessoaJuridica getPessoaJuridicaById(Long id) {		
		return template.getForObject(parametroService.getApiUrlPessoaJuridica() + "/id/" + id, PessoaJuridica.class);
	}
	
	public boolean isCnpjValido(String cnpj) {
		return new CNPJValidator().invalidMessagesFor(cnpj).isEmpty();
	}

}
