package br.com.wppatend.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import br.com.wppatend.constraints.DirecaoMensagem;

@Entity(name = "chat")
public class Chat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ChatSeq")
	@SequenceGenerator(name = "ChatSeq", sequenceName = "chatseq", allocationSize = 1)
	private Long idchat;
	private Long protocolo;
	private String tipo;
	private String mimetype;
	@Lob
	private String body;
	private String legenda;
	private DirecaoMensagem tx_rx;
	private Date data_tx_rx;
	public Long getIdchat() {
		return idchat;
	}
	public void setIdchat(Long idchat) {
		this.idchat = idchat;
	}
	public Long getProtocolo() {
		return protocolo;
	}
	public void setProtocolo(Long protocolo) {
		this.protocolo = protocolo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getLegenda() {
		return legenda;
	}
	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}
	public DirecaoMensagem getTx_rx() {
		return tx_rx;
	}
	public void setTx_rx(DirecaoMensagem tx_rx) {
		this.tx_rx = tx_rx;
	}
	public Date getData_tx_rx() {
		return data_tx_rx;
	}
	public void setData_tx_rx(Date data_tx_rx) {
		this.data_tx_rx = data_tx_rx;
	}
	
}
