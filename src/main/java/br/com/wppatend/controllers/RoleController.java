package br.com.wppatend.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.wppatend.entities.Permission;
import br.com.wppatend.entities.Role;
import br.com.wppatend.services.PermissionService;
import br.com.wppatend.services.RoleService;

@Controller
@RequestMapping("funcoes")
public class RoleController {
	
	private RoleService service;
	private PermissionService permissionService;
	
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	public RoleController(RoleService service, PermissionService permissionService) {
		this.service = service;
		this.permissionService = permissionService;
	}
	
	@GetMapping
    public String index() {
        return "redirect:/funcoes/1";
    }

    @GetMapping(value = "/{pageNumber}")
    public String list(@PathVariable Integer pageNumber, Model model) {
        Page<Role> page = service.getList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "funcoes/list";

    }

    @GetMapping("/add/funcao")
    public String add(Model model) {
    	Role r = new Role();
    	r.setPermissions(new ArrayList<>());
    	List<Permission> permissions = permissionService.getList();
        model.addAttribute("role", r);
        model.addAttribute("permissions", permissions);
        return "funcoes/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
    	Optional<Role> role = service.findById(id);
    	List<Permission> permissions = permissionService.getList();
        model.addAttribute("role", role.get());
        model.addAttribute("permissions", permissions);
        return "funcoes/form";

    }

    @PostMapping(value = "/save")
    public String save(Role role, final RedirectAttributes ra) {
    	String msg = "Função gravada com sucesso.";
        @SuppressWarnings("unused")
		Role nRole = service.save(role);
        ra.addFlashAttribute("successFlash", msg);
        return "redirect:/funcoes";

    }

	
	  @GetMapping("/delete/{id}")
	  public String delete(@PathVariable Long id) {
		  service.delete(id);
		  return "redirect:/funcoes";
	  }
	 

}
