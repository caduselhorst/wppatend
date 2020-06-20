package br.com.wppatend.entities;

import java.util.Date;

import javax.persistence.Column;
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
	@Column(columnDefinition = "text")
	private String msg_texto;
	@Lob
	@Column(length = 15360000)
	private byte[] msg_voz;
	@Lob
	@Column(length = 15360000)
	private byte[] msg_arquivo;
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
	public String getMsg_texto() {
		return msg_texto;
	}
	public void setMsg_texto(String msg_texto) {
		this.msg_texto = msg_texto;
	}
	public byte[] getMsg_voz() {
		return msg_voz;
	}
	public void setMsg_voz(byte[] msg_voz) {
		this.msg_voz = msg_voz;
	}
	public byte[] getMsg_arquivo() {
		return msg_arquivo;
	}
	public void setMsg_arquivo(byte[] msg_arquivo) {
		this.msg_arquivo = msg_arquivo;
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
