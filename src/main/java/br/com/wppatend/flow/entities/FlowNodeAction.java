package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="flownodeaction")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeAction extends FlowNode {

	private String actionClass;
	@OneToOne
	private FlowNode onSuccessNode;
	@OneToOne
	private FlowNode onErrorNode;
	
	public String getActionClass() {
		return actionClass;
	}
	
	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
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

	
}
