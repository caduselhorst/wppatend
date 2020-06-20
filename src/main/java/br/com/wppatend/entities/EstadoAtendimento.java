package br.com.wppatend.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "estadoatendimento")
public class EstadoAtendimento {
	
	@Id
	private Integer id;
	private String descricao;
	@Column(columnDefinition = "text")
	private String mensagem;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<EstadoAtendimentoDirecionamento> direcionamentos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public List<EstadoAtendimentoDirecionamento> getDirecionamentos() {
		return direcionamentos;
	}

	public void setDirecionamentos(List<EstadoAtendimentoDirecionamento> direcionamentos) {
		this.direcionamentos = direcionamentos;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EstadoAtendimento) {
			return id.equals(((EstadoAtendimento) obj).getId());
		}
		return false;
	}

}
