package br.com.wppatend.flow.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.entities.FlowInstanceParameter;
import br.com.wppatend.flow.entities.FlowInstancePhoneNumber;
import br.com.wppatend.flow.entities.FlowNode;
import br.com.wppatend.flow.entities.FlowNodeMenuOption;
import br.com.wppatend.flow.entities.FlowParameter;

public interface FlowService {
	
	public FlowInstancePhoneNumber saveFlowInstancePhoneNumber(FlowInstancePhoneNumber instance);
	public Optional<FlowInstancePhoneNumber> findFlowInstancePhoneNumberByPhoneNumber(String id);
	public void deleteFlowIntancePhoneNumber(FlowInstancePhoneNumber flowInstancePhoneNumber);
	
	public Flow saveFlow(Flow flow);
	public Flow addParameterFlowIntoFlow(Long flowId, FlowParameter parameter);
	public Flow addNodeIntoFlow(Long flowId, FlowNode node);
	public Optional<Flow> findFlowById(Long id);
	public Flow loadActiveFlow();
	public Page<Flow> getFlowList(Integer pageNumber);
	public void deleteFlow(Long id);
	
	public FlowInstance saveFlowInstance(FlowInstance flowInstance);
	public Optional<FlowInstance> findFlowInstanceById(Long id);
	public FlowInstance findFlowInstanceByProtocolo(Protocolo protocolo);
	
	public FlowNode saveFlowNode(FlowNode flowNode);
	public FlowNode addOptionIntoNodeMenu(Long nodeId, FlowNodeMenuOption option);
	public Optional<FlowNode> findFlowNodeById(Long id);
	
	public FlowNodeMenuOption saveFlowNodeMenuOption(FlowNodeMenuOption menuOption);
	public Optional<FlowNodeMenuOption> findFlowNodeMenuOptionById(Long id);
	
	public FlowParameter saveFlowParameter(FlowParameter flowParameter);
	public Optional<FlowParameter> findFlowParameterById(Long id);
	public void deleteFlowParameter(Long id);
	
	
	public List<FlowNode> loadNodeByFlow(Long flowId);
	public List<FlowParameter> loadParametersByFlow(Long flowId);
	public List<FlowNodeMenuOption> loadMenuOptionByNodeId(Long nodeId);
	
	public FlowInstanceParameter saveFlowInstanceParameter(FlowInstanceParameter flowInstanceParameter);
	public List<FlowInstanceParameter> findByFlowInstance(FlowInstance flowInstance);

}
