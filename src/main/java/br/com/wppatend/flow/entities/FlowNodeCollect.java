package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="flownodecollect")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeCollect extends FlowNode {

	private String message;
	private String collectedValue;
	@OneToOne
	private FlowNode nextNode;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getCollectedValue() {
		return collectedValue;
	}
	
	public void setCollectedValue(String collectedValue) {
		this.collectedValue = collectedValue;
	}
	
	public FlowNode getNextNode() {
		return nextNode;
	}
	
	public void setNextNode(FlowNode nextNode) {
		this.nextNode = nextNode;
	}
	
	
}
