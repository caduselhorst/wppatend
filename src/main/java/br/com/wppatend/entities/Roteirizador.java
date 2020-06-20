package br.com.wppatend.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "roteirizador")
public class Roteirizador {
	
	@Id
	private Long userId;
	private boolean disponivel;
	private boolean emAtendimento;
	private Date data;
	private Integer nroAtendimentos;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public boolean isDisponivel() {
		return disponivel;
	}
	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}
	public boolean isEmAtendimento() {
		return emAtendimento;
	}
	public void setEmAtendimento(boolean emAtendimento) {
		this.emAtendimento = emAtendimento;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Integer getNroAtendimentos() {
		return nroAtendimentos;
	}
	public void setNroAtendimentos(Integer nroAtendimentos) {
		this.nroAtendimentos = nroAtendimentos;
	}
	

}
