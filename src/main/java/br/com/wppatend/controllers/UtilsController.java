package br.com.wppatend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.wppatend.entities.Parametro;
import br.com.wppatend.services.ParametroService;

@RestController
public class UtilsController {
	
	@Autowired
	private ParametroService parametroService;
	
	@RequestMapping(value="/web/parametros/savelist", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public String salvaLista(RequestEntity<List<Parametro>> parametros) {
		
		parametros.getBody().forEach(parametro -> {
			parametroService.grava(parametro);
		});
		
		return "OK";
	}
	
	@RequestMapping(value="/web/parametros/save", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public String salva(RequestEntity<Parametro> parametro) {
		
		parametroService.grava(parametro.getBody());
		
		return "OK";
	}
	
	@RequestMapping(value="/web/parametros/feriado", method = RequestMethod.GET)
	public String isFeriado() {
		return String.valueOf(parametroService.isFeriado());
	}
	
	@RequestMapping(value="/web/parametros/horarioatendimento", method = RequestMethod.GET)
	public String isHorarioAtendimento() {
		return String.valueOf(parametroService.isHorarioAtendimento());
	}

}
