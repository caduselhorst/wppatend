package br.com.wppatend.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.entities.FilaAtendimento;
import br.com.wppatend.entities.Roteirizador;
import br.com.wppatend.entities.User;
import br.com.wppatend.services.FilaAtendimentoService;
import br.com.wppatend.services.FinalizacaoService;
import br.com.wppatend.services.RoteirizadorService;
import br.com.wppatend.services.SecurityService;
import br.com.wppatend.services.UserService;
import br.com.wppatend.validators.UserValidator;
import br.com.wppatend.vos.DashboardFila;
import br.com.wppatend.vos.DashboardUsuario;
import br.com.wppatend.vos.ITotalizadorFinalizacao;
import br.com.wppatend.wpprequest.model.PessoaFisica;

@Controller
public class LoginController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private RoteirizadorService roteirizadorService;
    
    @Autowired
    private FilaAtendimentoService filaAtendimentoService;
    
    @Autowired
    private PessoaFisicaRestClient pessoaFisicaRestClient;
    
    @Autowired
    private FinalizacaoService finalizacaoService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:dashboard/index";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        
        return "login";
    }

    @GetMapping({"/", "dashboard"})
    public String welcome(Model model) {
    	
    	List<Roteirizador> roteirizador = roteirizadorService.listAll();
    	
    	List<DashboardUsuario> usuarios = new ArrayList<>();
    	
    	roteirizador.forEach(r -> {
    		User u = userService.loadById(r.getUserId()).get();
    		DashboardUsuario du = new DashboardUsuario();
    		du.setUser(u);
    		du.setEmAtendimento(r.isEmAtendimento());
    		du.setDisponivel(r.isDisponivel());
    		du.setNroAtendimentos(r.getNroAtendimentos());
    		usuarios.add(du);
    	});
    	
    	List<FilaAtendimento> fila =  filaAtendimentoService.findAll();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	List<DashboardFila> lFila = new ArrayList<>();
    	fila.forEach(f -> {
    		
    		PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(f.getProtocolo().getCodPessoa());
    		
    		DashboardFila df = new DashboardFila();
    		df.setData(sdf.format(f.getDataFila()));
    		df.setFoneContato(f.getProtocolo().getFone());
    		df.setNomeContato(pf.getNome());
    		df.setNroProtocolo(f.getProtocolo().getProtocolo());
    		lFila.add(df);
    	});
    	
    	List<ITotalizadorFinalizacao> finalizacoes = finalizacaoService.countFinalizacoes();
    	Map<String, Long> map = new HashMap<>();
    	finalizacoes.forEach(f -> {
    		map.put(f.getFinalizacao(), f.getTotalFinalizacao());
    	});
    	
    	model.addAttribute("usuarios", usuarios);
    	model.addAttribute("fila", lFila);
    	model.addAttribute("finalizacoes", map);
    	
        return "dashboard/index";
    }

}
