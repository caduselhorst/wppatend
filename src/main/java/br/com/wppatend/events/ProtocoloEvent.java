package br.com.wppatend.events;

import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocoloEvent {
	
	private Protocolo protocolo;
	private User user;

}
