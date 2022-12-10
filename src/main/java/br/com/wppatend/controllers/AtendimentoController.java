package br.com.wppatend.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wppatend.entities.Roteirizador;
import br.com.wppatend.services.FinalizacaoService;
import br.com.wppatend.services.RoteirizadorService;
import br.com.wppatend.services.UserService;

@Controller
@RequestMapping("atendimentos")
public class AtendimentoController {
	
	private static final Logger logger = LoggerFactory.getLogger(AtendimentoController.class);
	
	@Autowired
	private RoteirizadorService roteirizadorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FinalizacaoService finalizacaoService;
	
	@GetMapping
    public String index() {
		logger.info("Index de Atendimentos");
        return "redirect:/atendimentos/";
    }
	
	
	@GetMapping("/")
    public String atendimento(Model model) {
		logger.info("Index de Atendimentos");
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = ((User) principal);
		String name = userService.findByUserName(user.getUsername()).getName();
		
		Optional<Roteirizador> roteirizador = roteirizadorService.loadById(userService.findByUserName(user.getUsername()).getId());
		
		if(roteirizador.isEmpty()) {
			model.addAttribute("disponivel", "0");
			model.addAttribute("ematendimento", "0");
			model.addAttribute("usuarioId", "0");
			model.addAttribute("nroAtendimentos", "0");
			
		} else {
			
			Roteirizador rot = roteirizador.get();
			
			model.addAttribute("disponivel", rot.isDisponivel() ? "1" : "0");
			model.addAttribute("ematendimento", rot.isEmAtendimento() ? "1" : "0");
			model.addAttribute("usuarioId", rot.getUserId());
			model.addAttribute("nroAtendimentos", rot.getNroAtendimentos());
			
		}
		
		model.addAttribute("usuarioNome", name);
		model.addAttribute("finalizacoes", finalizacaoService.findAll());
				
        return "atendimentos/list";
    }
}
