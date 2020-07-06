package br.com.wppatend.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import br.com.wppatend.entities.User;
import br.com.wppatend.services.RoleService;
import br.com.wppatend.services.UserService;
import br.com.wppatend.vos.AlterarSenhaVO;

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
    
    @GetMapping("/alterasenha")
    public String alterarSenha(@Param(value = "user") String username, Model model) {
    	User u = userService.findByUserName(username);
    	
    	AlterarSenhaVO vo = new AlterarSenhaVO();
    	vo.setIdUsuario(u.getId());
    	    	
        model.addAttribute("vo", vo);
        return "usuarios/alterasenha";

    }

    @PostMapping(value = "/save")
    public RedirectView save(User user, RedirectAttributes ra) {
    	String msg = "Usuário salvo com sucesso.";
    	if(user.getId() == null) {
    		user.setPassword(new BCryptPasswordEncoder().encode("1234"));
    		msg += " Foi atribuída a senha 1234 para o usuário";
    	}
        @SuppressWarnings("unused")
		User save = userService.save(user);
        ra.addFlashAttribute("successFlash", msg);
        return new RedirectView("/usuarios");

    }
    
    @PostMapping(value = "/alteraSenha")
    public RedirectView changePassword(@ModelAttribute AlterarSenhaVO model, RedirectAttributes ra) {
    	//String msg = "Usuário salvo com sucesso.";
    	Optional<User> oUser = userService.loadById(model.getIdUsuario());
    	if(model.getNewPassword().equals(model.getConfirmPassword())) {
    		oUser.get().setPassword(new BCryptPasswordEncoder().encode(model.getNewPassword()));
    		userService.save(oUser.get());
    		return new RedirectView("/");
    	} else {
    		model.setErro("As senhas não conferem");
    		return new RedirectView("/usuarios/alterasenha/" + oUser.get().getUsername());
    	}
    	        

    }

	
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
    	Optional<User> oUser = userService.loadById(id);
    	userService.delete(oUser.get());
    	return "redirect:/usuarios";
    }
	

}
