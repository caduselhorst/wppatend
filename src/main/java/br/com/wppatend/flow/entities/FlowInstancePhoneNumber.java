package br.com.wppatend.flow.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "flowinstancephonenumber")
public class FlowInstancePhoneNumber {
	
	@Id
	@Column(columnDefinition = "varchar(30)")
	private String phoneNumber;
	@OneToOne
	private FlowInstance flowInstance;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public FlowInstance getFlowInstance() {
		return flowInstance;
	}
	
	public void setFlowInstance(FlowInstance flowInstance) {
		this.flowInstance = flowInstance;
	}
	
	
	

}
