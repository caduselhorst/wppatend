package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="flownodemenu")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeMenu extends FlowNode {
	
	private String message;
	/*
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
	private List<FlowNodeMenuOption> options;
	*/
	private String messageOnUnrecognizedOption;
	@OneToOne
	private FlowNode unrecognizedOptionNode;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}	
	
	public String getMessageOnUnrecognizedOption() {
		return messageOnUnrecognizedOption;
	}
	
	public void setMessageOnUnrecognizedOption(String messageOnUnrecognizedOption) {
		this.messageOnUnrecognizedOption = messageOnUnrecognizedOption;
	}

	public FlowNode getUnrecognizedOptionNode() {
		return unrecognizedOptionNode;
	}

	public void setUnrecognizedOptionNode(FlowNode unrecognizedOptionNode) {
		this.unrecognizedOptionNode = unrecognizedOptionNode;
	}

}
