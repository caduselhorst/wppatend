package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="flownodemessage")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeMessage extends FlowNode {
	
	private String message;
	@OneToOne
	private FlowNode nextNode;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public FlowNode getNextNode() {
		return nextNode;
	}
	
	public void setNextNode(FlowNode nextNode) {
		this.nextNode = nextNode;
	}
	
}
