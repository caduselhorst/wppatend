package br.com.wppatend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.entities.Chat;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.repositories.ChatRepository;
import br.com.wppatend.repositories.ProtocoloRepository;
import br.com.wppatend.repositories.UserRepository;
import br.com.wppatend.wpprequest.model.PessoaFisica;
import br.com.wppatend.wpprequest.model.PessoaJuridica;

@Controller
@RequestMapping("atendimentos")
public class AtendimentoController {
	
	private static final Logger logger = LoggerFactory.getLogger(AtendimentoController.class);
	
	@Autowired
	private ProtocoloRepository protocoloRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PessoaJuridicaRestClient pessoaJuridicaRestClient;
	
	@Autowired
	private PessoaFisicaRestClient pessoaFisicaRestClient;
	
	@Autowired
	private ChatRepository chatRepository;
	
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
		
		Protocolo p = protocoloRepository.fingProtocoloAbertoByOperador(userRepository.findByUsername(user.getUsername()).getId());
		PessoaFisica pf = null;
		PessoaJuridica pj = null;
		if(p != null) {
			
			if(p.getCodPessoa() != null) {
				pf = pessoaFisicaRestClient.getPessoaFisicaById(p.getCodPessoa());
			}
			if(p.getCodPessoaJuridica() != null) {
				pj = pessoaJuridicaRestClient.getPessoaJuridicaById(p.getCodPessoaJuridica());
			}
		}
		
		if(pf == null) {
			pf = new PessoaFisica();
		}
		if(pj == null) {
			pj = new PessoaJuridica();
		}
		
		if(p == null) {
			p = new Protocolo();
		}
		
		List<Chat> chat = chatRepository.findByprotocolo(p.getId());
		
		model.addAttribute("protocolo", p);
		model.addAttribute("pj", pj);
		model.addAttribute("pf", pf);
		model.addAttribute("chat", chat);
		
		
        return "atendimentos/list";
    }
}
