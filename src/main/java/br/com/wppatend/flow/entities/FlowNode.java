package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "flownode")
@Inheritance(strategy = InheritanceType.JOINED)
public class FlowNode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FlowNodeSeq")
	@SequenceGenerator(name = "FlowNodeSeq", sequenceName = "flownodeseq", allocationSize = 1)
	private Long id;
	private String name;
	@OneToOne
	private Flow flow;
	private boolean copyParamToNextNode;
	private boolean deleted;
	private boolean end;
	private boolean init;
	
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
	
	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}
	

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
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
		FlowNode other = (FlowNode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
