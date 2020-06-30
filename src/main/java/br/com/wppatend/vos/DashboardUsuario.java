package br.com.wppatend.vos;

import br.com.wppatend.entities.User;

public class DashboardUsuario {
	
	private User user;
	private boolean disponivel;
	private boolean emAtendimento;
	private int nroAtendimentos;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public int getNroAtendimentos() {
		return nroAtendimentos;
	}
	public void setNroAtendimentos(int nroAtendimentos) {
		this.nroAtendimentos = nroAtendimentos;
	}
	
	
}
