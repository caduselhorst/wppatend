package br.com.wppatend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import br.com.wppatend.entities.User;
import br.com.wppatend.repositories.RoteirizadorRepository;
import br.com.wppatend.repositories.UserRepository;

public class LogoutSuccessListener {
	
	private static final Logger logger = LoggerFactory.getLogger(LogoutSuccessListener.class);
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoteirizadorRepository roteirizadorRepository;
	
	@EventListener
	public void handleEvent(Object event) {
		
		if(event instanceof LogoutSuccessEvent) {
			logger.info("Novo evento de logout: Usuário[" + ((LogoutSuccessEvent) event).getAuthentication().getName() + "]");
			boolean isOperador = false;
			for(GrantedAuthority ga : ((LogoutSuccessEvent) event).getAuthentication().getAuthorities()) {
				if(ga.getAuthority().equals("Operador")) {
					isOperador = true;
				}
			}
			
			if(isOperador) {
				User user = userRepository.findByUsername(((LogoutSuccessEvent) event).getAuthentication().getName());
				roteirizadorRepository.delete(roteirizadorRepository.findById(user.getId()).get());
				logger.info("Usuário retirado da roteirização");
			}
			
		}
	}

}
