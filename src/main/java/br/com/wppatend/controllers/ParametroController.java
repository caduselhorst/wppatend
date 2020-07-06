package br.com.wppatend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.wppatend.entities.Parametro;
import br.com.wppatend.services.ParametroService;

@Controller
@RequestMapping("parametros")
public class ParametroController {
	
	@Autowired
	private ParametroService parametroService;
	
	@GetMapping
    public String index() {
        return "redirect:/parametros/1";
    }
	
	@GetMapping(value = "/{pageNumber}")
    public String list(@PathVariable Integer pageNumber, Model model) {
        Page<Parametro> page = parametroService.getList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "parametros/list";

    }
	
	@GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
    	Optional<Parametro> f = parametroService.carrega(id);
        model.addAttribute("parametro", f.get());
        return "parametros/form";

    }
	
	@PostMapping(value = "/save")
    public String save(Parametro parametro, final RedirectAttributes ra) {
    	String msg = "Parâmetro salvo com sucesso.";
		@SuppressWarnings("unused")
		Parametro save = parametroService.grava(parametro);
        ra.addFlashAttribute("successFlash", msg);
        return "redirect:/parametros";

    }
	
	

}
