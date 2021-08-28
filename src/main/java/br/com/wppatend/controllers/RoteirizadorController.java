package br.com.wppatend.controllers;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wppatend.roteirizador.RoteirizadorThread;

@Controller
@RequestMapping("roteirizador")
public class RoteirizadorController {
	
	//private static final Logger logger = LoggerFactory.getLogger(AtendimentoController.class);
	
	@Autowired
	private RoteirizadorThread roteirizador;
	
	@GetMapping
    public String index() {
		//logger.info("Index de Atendimentos");
        return "redirect:/roteirizador/";
    }
	
	@GetMapping("/")
    public String roteirizador(Model model) {
		
		model.addAttribute("roteirizador", roteirizador);
		model.addAttribute("status", (roteirizador.getThread() == null ? false : roteirizador.getThread().isAlive()));
		
		return "roteirizador/show";
		
    }
	
	@GetMapping("/iniciar")
    public String iniciar(Model model) {
		
		roteirizador.setThread(new Thread(roteirizador));
		roteirizador.getThread().start();
		
		model.addAttribute("roteirizador", roteirizador);
		model.addAttribute("status", (roteirizador.getThread() == null ? false : roteirizador.getThread().isAlive()));
		
		return "redirect:/roteirizador/";
		
    }
	
	@GetMapping("/parar")
    public String parar(Model model) {
		
		roteirizador.finalizar();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.err.println("Interrupção de thread");
		}
		
		model.addAttribute("roteirizador", roteirizador);
		model.addAttribute("status", (roteirizador.getThread() == null ? false : roteirizador.getThread().isAlive()));
		
		return "redirect:/roteirizador/";
		
    }

}
