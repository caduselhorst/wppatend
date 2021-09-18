package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="flownodeparameter")
public class FlowNodeParameter {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FlowNodeParamSeq")
	@SequenceGenerator(name = "FlowNodeParamSeq", sequenceName = "flownodeparamseq", allocationSize = 1)
	private Long id;
	private String name;
	private String value;
	private boolean deleted;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
