package br.com.wppatend.controllers;

import java.util.Optional;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.wppatend.entities.Departamento;
import br.com.wppatend.services.DepartamentoService;

@Controller
@RequestMapping("departamentos")
public class DepartamentoController {
	
	//private static final Logger logger = LoggerFactory.getLogger(DepartamentoController.class);
	
	@Autowired
	private DepartamentoService departamentoService;
	
	@GetMapping
    public String index() {
        return "redirect:/departamentos/1";
    }
	
	@GetMapping(value = "/{pageNumber}")
    public String list(@PathVariable Integer pageNumber, Model model) {
		//logger.debug("Entrou na lista de departamentos");
        Page<Departamento> page = departamentoService.getPaginatedList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "departamentos/list";

    }
	
	@GetMapping("/add/departamento")
    public String add(Model model) {
		//logger.debug("Entrou em add de departamentos");
    	Departamento f = new Departamento();
        model.addAttribute("departamento", f);
        return "departamentos/form";

    }
	
	@GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
		//logger.debug("Entrou edit de finalizações");
    	Optional<Departamento> f = departamentoService.loadById(id);
        model.addAttribute("departamento", f.get());
        return "departametnos/form";

    }
	
	@PostMapping(value = "/save")
    public String save(Departamento departamento, final RedirectAttributes ra) {
		//logger.debug("Entrou em save de departamentos");
    	String msg = "Finalização salva com sucesso.";
		@SuppressWarnings("unused")
		Departamento save = departamentoService.save(departamento);
        ra.addFlashAttribute("successFlash", msg);
        return "redirect:/departamentos";

    }
	
	@GetMapping("/delete/{id}")
	  public String delete(@PathVariable Long id) {
		  departamentoService.delete(departamentoService.loadById(id).get());
		  return "redirect:/departamentos";
	  }

}
