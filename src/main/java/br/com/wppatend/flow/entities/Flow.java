package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "flow")
public class Flow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FlowSeq")
	@SequenceGenerator(name = "FlowSeq", sequenceName = "flowseq", allocationSize = 1)
	private Long id;
	private String name;
	@OneToOne(optional = true)
	private FlowNode initialNode;
	private boolean active;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public FlowNode getInitialNode() {
		return initialNode;
	}
	public void setInitialNode(FlowNode initialNode) {
		this.initialNode = initialNode;
	}
	

}
