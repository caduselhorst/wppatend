package br.com.wppatend.flow.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="flownodedecision")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeDecision extends FlowNode {
	
	private String decisionClass;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private FlowNode onTrueNode;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private FlowNode onFalseNode;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private FlowNode onErrorNode;
	
	public String getDecisionClass() {
		return decisionClass;
	}
	public void setDecisionClass(String decisionClass) {
		this.decisionClass = decisionClass;
	}
	public FlowNode getOnTrueNode() {
		return onTrueNode;
	}
	public void setOnTrueNode(FlowNode onTrueNode) {
		this.onTrueNode = onTrueNode;
	}
	public FlowNode getOnFalseNode() {
		return onFalseNode;
	}
	public void setOnFalseNode(FlowNode onFalseNode) {
		this.onFalseNode = onFalseNode;
	}
	public FlowNode getOnErrorNode() {
		return onErrorNode;
	}
	public void setOnErrorNode(FlowNode onErrorNode) {
		this.onErrorNode = onErrorNode;
	}

	
}
