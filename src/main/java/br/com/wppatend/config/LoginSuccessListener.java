package br.com.wppatend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import br.com.wppatend.entities.Roteirizador;
import br.com.wppatend.repositories.RoteirizadorRepository;
import br.com.wppatend.repositories.UserRepository;


public class LoginSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	private static final Logger logger = LoggerFactory.getLogger(LoginSuccessListener.class);
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoteirizadorRepository roteirizadorRepository;
	
	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = ((User) principal);
		logger.info("Usuário logado: " + user.getUsername());
		boolean isOperador = false;
		
		for(GrantedAuthority ga : user.getAuthorities()) {
			if(ga.getAuthority().equals("Operador")) {
				isOperador = true;
			}
		}
		
		if(isOperador) {
			br.com.wppatend.entities.User eUser = userRepository.findByUsername(user.getUsername());
			Roteirizador rot = new Roteirizador();
			rot.setDisponivel(false);
			rot.setEmAtendimento(false);
			rot.setUserId(eUser.getId());
			roteirizadorRepository.save(rot);
			logger.info("Usuário operador adicionado à roteirização");
		}
	}

}
