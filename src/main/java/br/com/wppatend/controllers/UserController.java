package br.com.wppatend.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.wppatend.entities.User;
import br.com.wppatend.services.RoleService;
import br.com.wppatend.services.UserService;

@Controller
@RequestMapping("usuarios")
public class UserController {
	
	private UserService userService;
	private RoleService roleService;
	
	public UserController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}
	
	@GetMapping
    public String index() {
        return "redirect:/usuarios/1";
    }

    @GetMapping(value = "/{pageNumber}")
    public String list(@PathVariable Integer pageNumber, Model model) {
        Page<User> page = userService.getList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "usuarios/list";

    }

    @GetMapping("/add/usuario")
    public String add(Model model) {
    	User u = new User();
    	u.setRoles(new ArrayList<>());
    	model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", u);
        return "usuarios/form";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
    	Optional<User> u = userService.loadById(id);
    	model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", u.get());
        return "usuarios/form";

    }

    @PostMapping(value = "/save")
    public String save(User user, final RedirectAttributes ra) {
    	String msg = "Usuário salvo com sucesso.";
    	if(user.getId() == null) {
    		user.setPassword(new BCryptPasswordEncoder().encode("1234"));
    		msg += " Foi atribuída a senha 1234 para o usuário";
    	}
        @SuppressWarnings("unused")
		User save = userService.save(user);
        ra.addFlashAttribute("successFlash", msg);
        return "redirect:/usuarios";

    }

	/*
	 * @GetMapping("/delete/{id}") public String delete(@PathVariable Long id) {
	 * 
	 * customerService.delete(id); return "redirect:/customers";
	 * 
	 * }
	 */

}
