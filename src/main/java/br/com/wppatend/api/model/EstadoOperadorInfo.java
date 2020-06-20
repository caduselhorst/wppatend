package br.com.wppatend.api.model;

public class EstadoOperadorInfo {
	
	private Long idUser;
	private Boolean disponivel;
	private Boolean emAtendimento;
	
	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	public Boolean getDisponivel() {
		return disponivel;
	}
	public void setDisponivel(Boolean disponivel) {
		this.disponivel = disponivel;
	}
	public Boolean getEmAtendimento() {
		return emAtendimento;
	}
	public void setEmAtendimento(Boolean emAtendimento) {
		this.emAtendimento = emAtendimento;
	}
	
}
