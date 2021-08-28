package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name="flowinstanceparameter")
public class FlowInstanceParameter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FlowInstanceParameterSeq")
	@SequenceGenerator(name = "FlowInstanceParameterSeq", sequenceName = "flowinstparamseq", allocationSize = 1)
	private Long id;
	@OneToOne
	private FlowParameter parameter;
	@Lob
	private byte[] value;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public FlowParameter getParameter() {
		return parameter;
	}
	
	public void setParameter(FlowParameter parameter) {
		this.parameter = parameter;
	}
	
	public byte[] getValue() {
		return value;
	}
	
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlowInstanceParameter other = (FlowInstanceParameter) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
