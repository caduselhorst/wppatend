package br.com.wppatend.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.wppatend.entities.Finalizacao;
import br.com.wppatend.services.FinalizacaoService;

@Controller
@RequestMapping("finalizacoes")
public class FinalizacaoController {
	
	private static final Logger logger = LoggerFactory.getLogger(FinalizacaoController.class);
	
	@Autowired
	private FinalizacaoService finalizacaoService;
	
	@GetMapping
    public String index() {
        return "redirect:/finalizacoes/1";
    }
	
	@GetMapping(value = "/{pageNumber}")
    public String list(@PathVariable Integer pageNumber, Model model) {
		logger.debug("Entrou na lista de finalizações");
        Page<Finalizacao> page = finalizacaoService.getList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "finalizacoes/list";

    }
	
	@GetMapping("/add/finalizacao")
    public String add(Model model) {
		logger.debug("Entrou em add de finalizações");
    	Finalizacao f = new Finalizacao();
        model.addAttribute("finalizacao", f);
        return "finalizacoes/form";

    }
	
	@GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
		logger.debug("Entrou edit de finalizações");
    	Optional<Finalizacao> f = finalizacaoService.loadById(id);
        model.addAttribute("finalizacao", f.get());
        return "finalizacoes/form";

    }
	
	@PostMapping(value = "/save")
    public String save(Finalizacao finalizacao, final RedirectAttributes ra) {
		logger.debug("Entrou em save de finalizações");
    	String msg = "Finalização salva com sucesso.";
		@SuppressWarnings("unused")
		Finalizacao save = finalizacaoService.save(finalizacao);
        ra.addFlashAttribute("successFlash", msg);
        return "redirect:/finalizacoes";

    }
	
	@GetMapping("/delete/{id}")
	  public String delete(@PathVariable Long id) {
		  finalizacaoService.delete(finalizacaoService.loadById(id).get());
		  return "redirect:/finalizacoes";
	  }

}
