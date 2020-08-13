package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import br.com.wppatend.entities.Departamento;

@Entity(name="flownodeenqueue")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeEnqueue extends FlowNode {

	private String message;
	@OneToOne
	private Departamento departamento;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Departamento getDepartamento() {
		return departamento;
	}
	
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	
	
}
