package br.com.wppatend.flow.services;

import java.util.Optional;

import org.springframework.data.domain.Page;

import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.entities.FlowInstancePhoneNumber;
import br.com.wppatend.flow.entities.FlowNode;

public interface FlowService {
	
	public FlowInstancePhoneNumber saveFlowInstancePhoneNumber(FlowInstancePhoneNumber instance);
	public Optional<FlowInstancePhoneNumber> findFlowInstancePhoneNumberByPhoneNumber(String id);
	public void deleteFlowIntancePhoneNumber(FlowInstancePhoneNumber flowInstancePhoneNumber);
	
	public Flow saveFlow(Flow flow);
	public Optional<Flow> findFlowById(Long id);
	public Flow loadActiveFlow();
	public Page<Flow> getFlowList(Integer pageNumber);
	public void deleteFlow(Long id);
	
	public FlowInstance saveFlowInstance(FlowInstance flowInstance);
	public Optional<FlowInstance> findFlowInstanceById(Long id);
	public FlowInstance findFlowInstanceByProtocolo(Protocolo protocolo);
	
	public FlowNode saveFlowNode(FlowNode flowNode);
	public Optional<FlowNode> findFlowNodeById(Long id);

}
