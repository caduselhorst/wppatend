package br.com.wppatend.flow.services;

import java.util.Optional;

import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.entities.FlowInstancePhoneNumber;
import br.com.wppatend.flow.entities.FlowNode;

public interface FlowService {
	
	public FlowInstancePhoneNumber saveFlowInstancePhoneNumber(FlowInstancePhoneNumber instance);
	public Optional<FlowInstancePhoneNumber> findFlowInstancePhoneNumberByPhoneNumber(String id);
	
	public Flow saveFlow(Flow flow);
	public Optional<Flow> findFlowById(Long id);
	public Flow loadActiveFlow();
	
	public FlowInstance saveFlowInstance(FlowInstance flowInstance);
	public Optional<FlowInstance> findFlowInstanceById(Long id);
	
	public FlowNode saveFlowNode(FlowNode flowNode);
	public Optional<FlowNode> findFlowNodeById(Long id);

}
