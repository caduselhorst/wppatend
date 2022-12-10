package br.com.wppatend.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("configuracoes")
public class ConfiguracaoApiController {
	
	
	@GetMapping
    public String index() {
		//logger.info("Index de Atendimentos");
        return "redirect:/roteirizador/";
    }
	
	@GetMapping("/qrcode")
    public String roteirizador(Model model) {
			
		return "configuracoes/qrcode-form";	
    }
	
	@GetMapping("/webhook")
    public String webHook(Model model) {
			
		return "configuracoes/webhook-form";	
    }
	
	
	@GetMapping("/acoes")
    public String acoes(Model model) {
			
		return "configuracoes/outras-form";	
    }

}
