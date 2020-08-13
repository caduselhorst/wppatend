package br.com.wppatend.flow.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="flownodemenu")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeMenu extends FlowNode {
	
	private String message;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<FlowNodeMenuOption> options;
	private String messageOnUnrecognizedOption;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<FlowNodeMenuOption> getOptions() {
		return options;
	}
	
	public void setOptions(List<FlowNodeMenuOption> options) {
		this.options = options;
	}
	
	public String getMessageOnUnrecognizedOption() {
		return messageOnUnrecognizedOption;
	}
	
	public void setMessageOnUnrecognizedOption(String messageOnUnrecognizedOption) {
		this.messageOnUnrecognizedOption = messageOnUnrecognizedOption;
	}

}
