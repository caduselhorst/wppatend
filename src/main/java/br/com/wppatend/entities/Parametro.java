package br.com.wppatend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="parametro")
public class Parametro {
	
	@Id
	@Column(columnDefinition = "varchar(30)")
	private String chave;
	private String valor;
	
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

}
