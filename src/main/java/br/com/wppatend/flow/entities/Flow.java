package br.com.wppatend.flow.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "flow")
@SQLDelete(sql = "update flow set deleted = true where id=?")
@Where(clause = "deleted = false")
public class Flow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FlowSeq")
	@SequenceGenerator(name = "FlowSeq", sequenceName = "flowseq", allocationSize = 1)
	private Long id;
	private String name;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
	private List<FlowNode> nodes;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
	private List<FlowParameter> parameters;
	@OneToOne
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
	public List<FlowNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<FlowNode> nodes) {
		this.nodes = nodes;
	}
	public List<FlowParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<FlowParameter> parameters) {
		this.parameters = parameters;
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
