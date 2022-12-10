package br.com.wppatend.flow.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="flownodecollect")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeCollect extends FlowNode {

	private String message;
	private String collectorClass;
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = true, optional = true)
	private FlowNode nextNode;
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = true, optional = true)
	private FlowNode onErrorNode;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getCollectorClass() {
		return collectorClass;
	}

	public void setCollectorClass(String collectorClass) {
		this.collectorClass = collectorClass;
	}

	public FlowNode getNextNode() {
		return nextNode;
	}
	
	public void setNextNode(FlowNode nextNode) {
		this.nextNode = nextNode;
	}

	public FlowNode getOnErrorNode() {
		return onErrorNode;
	}

	public void setOnErrorNode(FlowNode onErrorNode) {
		this.onErrorNode = onErrorNode;
	}
	
}
