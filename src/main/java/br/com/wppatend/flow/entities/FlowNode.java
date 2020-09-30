package br.com.wppatend.flow.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "flownode")
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "update flownode set deleted = true where id=?")
@Where(clause = "deleted = false")
public class FlowNode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FlowNodeSeq")
	@SequenceGenerator(name = "FlowNodeSeq", sequenceName = "flownodeseq", allocationSize = 1)
	private Long id;
	private String name;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FlowNodeParameter> params;
	private boolean copyParamToNextNode;
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

	public List<FlowNodeParameter> getParams() {
		return params;
	}

	public void setParams(List<FlowNodeParameter> params) {
		this.params = params;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isCopyParamToNextNode() {
		return copyParamToNextNode;
	}

	public void setCopyParamToNextNode(boolean copyParamToNextNode) {
		this.copyParamToNextNode = copyParamToNextNode;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FlowNode) {
			return ((FlowNode) obj).getId().equals(id);
		}
		return false;
	}

}
