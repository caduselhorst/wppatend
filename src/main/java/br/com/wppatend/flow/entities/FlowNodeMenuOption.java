package br.com.wppatend.flow.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "flownodemenuoption")
public class FlowNodeMenuOption {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String pattern;
	@OneToOne(cascade = CascadeType.MERGE)
	private FlowNodeMenu menuNode;
	@OneToOne(cascade = CascadeType.MERGE)
	private FlowNode nextNode;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public FlowNode getNextNode() {
		return nextNode;
	}
	
	public void setNextNode(FlowNode nextNode) {
		this.nextNode = nextNode;
	}

	public FlowNodeMenu getMenuNode() {
		return menuNode;
	}

	public void setMenuNode(FlowNodeMenu menuNode) {
		this.menuNode = menuNode;
	}
	
}
