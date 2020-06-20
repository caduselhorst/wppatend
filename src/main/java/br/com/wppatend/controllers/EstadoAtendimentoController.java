package br.com.wppatend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.wppatend.entities.EstadoAtendimento;
import br.com.wppatend.repositories.EstadoAtendimentoRespository;

@RestController
public class EstadoAtendimentoController {
	
	
	@Autowired
	private EstadoAtendimentoRespository repository;
	
	@RequestMapping(value="/web/estadoatendimento/savelist", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public String salvaLista(RequestEntity<List<EstadoAtendimento>> estados) {
		for(EstadoAtendimento ea : estados.getBody()) {
			repository.save(ea);
		}
		
		return "OK";
	}
	
	@RequestMapping(value="/estadoatendimento", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public String salva(EstadoAtendimento estado) {
		repository.save(estado);		
		return "OK";
	}

}
