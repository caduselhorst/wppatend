package br.com.wppatend.flow.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="flownodemessage")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeMessage extends FlowNode {

	private String message;
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = true, optional = true)
	private FlowNode onSuccessNode;
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = true, optional = true)
	private FlowNode onErrorNode;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FlowNode getOnSuccessNode() {
		return onSuccessNode;
	}
	
	public void setOnSuccessNode(FlowNode onSuccessNode) {
		this.onSuccessNode = onSuccessNode;
	}
	
	public FlowNode getOnErrorNode() {
		return onErrorNode;
	}

	public void setOnErrorNode(FlowNode onErrorNode) {
		this.onErrorNode = onErrorNode;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
