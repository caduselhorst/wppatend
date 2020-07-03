package br.com.wppatend.vos;

import java.util.List;

public class DashboardVO {
	
	private List<DashboardFila> fila;
	private List<DashboardUsuario> usuarios;
	private List<ITotalizadorFinalizacao> finalizacoes;
	public List<DashboardFila> getFila() {
		return fila;
	}
	public void setFila(List<DashboardFila> fila) {
		this.fila = fila;
	}
	public List<DashboardUsuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<DashboardUsuario> usuarios) {
		this.usuarios = usuarios;
	}
	public List<ITotalizadorFinalizacao> getFinalizacoes() {
		return finalizacoes;
	}
	public void setFinalizacoes(List<ITotalizadorFinalizacao> finalizacoes) {
		this.finalizacoes = finalizacoes;
	}
	

}
