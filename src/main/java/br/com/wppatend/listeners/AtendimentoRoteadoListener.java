package br.com.wppatend.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.wppatend.events.ProtocoloEvent;
import br.com.wppatend.notificadores.NotificadorAtendimentoRoteador;

@Component
public class AtendimentoRoteadoListener {
	
	@Autowired
	private NotificadorAtendimentoRoteador notificadorAtendimentoRoteado;
	
	@EventListener
	public void atendimentoRoteadoListener(ProtocoloEvent event) {
		notificadorAtendimentoRoteado.enviar(event.getProtocolo(), event.getUser());
	}

}
