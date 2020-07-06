package br.com.wppatend.vos;

import java.util.List;

public class DashboardVO {
	
	private List<DashboardFila> fila;
	private List<DashboardUsuario> usuarios;
	private List<ITotalizadorFinalizacao> finalizacoes;
	private List<ITotalizadorFinalizacao> finalizacoesOntem;
	private List<ITotalizadorFinalizacao> finalizacoesMesAtual;
	private List<ITotalizadorFinalizacao> finalizacoesMesAnterior;
	private List<ITotalizadorFinalizacao> finalizacoesAnoAtual;
	private List<ITotalizadorFinalizacao> finalizacoesAnoAnterior;
	private List<DashboardProcesso> processos;
	private DashboardApiStatus status;
	
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
	public List<DashboardProcesso> getProcessos() {
		return processos;
	}
	public void setProcessos(List<DashboardProcesso> processos) {
		this.processos = processos;
	}
	public DashboardApiStatus getStatus() {
		return status;
	}
	public void setStatus(DashboardApiStatus status) {
		this.status = status;
	}
	public List<ITotalizadorFinalizacao> getFinalizacoesOntem() {
		return finalizacoesOntem;
	}
	public void setFinalizacoesOntem(List<ITotalizadorFinalizacao> finalizacoesOntem) {
		this.finalizacoesOntem = finalizacoesOntem;
	}
	public List<ITotalizadorFinalizacao> getFinalizacoesMesAtual() {
		return finalizacoesMesAtual;
	}
	public void setFinalizacoesMesAtual(List<ITotalizadorFinalizacao> finalizacoesMesAtual) {
		this.finalizacoesMesAtual = finalizacoesMesAtual;
	}
	public List<ITotalizadorFinalizacao> getFinalizacoesMesAnterior() {
		return finalizacoesMesAnterior;
	}
	public void setFinalizacoesMesAnterior(List<ITotalizadorFinalizacao> finalizacoesMesAnterior) {
		this.finalizacoesMesAnterior = finalizacoesMesAnterior;
	}
	public List<ITotalizadorFinalizacao> getFinalizacoesAnoAtual() {
		return finalizacoesAnoAtual;
	}
	public void setFinalizacoesAnoAtual(List<ITotalizadorFinalizacao> finalizacoesAnoAtual) {
		this.finalizacoesAnoAtual = finalizacoesAnoAtual;
	}
	public List<ITotalizadorFinalizacao> getFinalizacoesAnoAnterior() {
		return finalizacoesAnoAnterior;
	}
	public void setFinalizacoesAnoAnterior(List<ITotalizadorFinalizacao> finalizacoesAnoAnterior) {
		this.finalizacoesAnoAnterior = finalizacoesAnoAnterior;
	}
	

}
