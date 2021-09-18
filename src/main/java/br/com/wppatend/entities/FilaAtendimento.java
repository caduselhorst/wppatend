package br.com.wppatend.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "filatendimento")
public class FilaAtendimento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FilaSeq")
	@SequenceGenerator(name = "FilaSeq", sequenceName = "filaseq", allocationSize = 1)
	private Long id;
	@OneToOne
	private Protocolo protocolo;
	@OneToOne
	private Departamento departamento;
	private Date dataFila;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Protocolo getProtocolo() {
		return protocolo;
	}
	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}
	public Date getDataFila() {
		return dataFila;
	}
	public void setDataFila(Date dataFila) {
		this.dataFila = dataFila;
	}
	public Departamento getDepartamento() {
		return departamento;
	}
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

}
