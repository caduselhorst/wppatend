package br.com.wppatend.notificadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.entities.User;

@Component
public class NotificadorAtendimentoRoteador {

	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
	public void enviar(Protocolo p, User user) {
		
		MessageWrapper wrapper = MessageWrapper.builder().type(MessageType.PROTOCOLO.toString()).message(p).build();
		
		simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/messages", wrapper);
	}
	
}
