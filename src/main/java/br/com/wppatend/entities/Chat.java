package br.com.wppatend.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import br.com.wppatend.constraints.DirecaoMensagem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "chat")
public class Chat {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idchat;
	private Long protocolo;
	private String tipo;
	private String mimetype;
	@Lob
	private String body;
	private String legenda;
	private DirecaoMensagem tx_rx;
	private Date data_tx_rx;
	private String chatId;
	private String messageId;
	
}