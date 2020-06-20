package br.com.wppatend.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "estadoatendimentodirecionamento")
public class EstadoAtendimentoDirecionamento {
	
	@Id
	private Integer id;
	private String resposta;
	private Integer idProximoEstado;
	private Integer idProximoEstadoErro;
	private Boolean confirmacao;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getResposta() {
		return resposta;
	}
	public void setResposta(String resposta) {
		this.resposta = resposta;
	}
	public Integer getIdProximoEstado() {
		return idProximoEstado;
	}
	public void setIdProximoEstado(Integer idProximoEstado) {
		this.idProximoEstado = idProximoEstado;
	}
	public Boolean getConfirmacao() {
		return confirmacao;
	}
	public void setConfirmacao(Boolean confirmacao) {
		this.confirmacao = confirmacao;
	}
	public Integer getIdProximoEstadoErro() {
		return idProximoEstadoErro;
	}
	public void setIdProximoEstadoErro(Integer idProximoEstadoErro) {
		this.idProximoEstadoErro = idProximoEstadoErro;
	}
	

}
