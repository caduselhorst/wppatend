package br.com.wppatend.flow.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.entities.FlowInstancePhoneNumber;
import br.com.wppatend.flow.entities.FlowNode;
import br.com.wppatend.flow.entities.FlowNodeMenuOption;
import br.com.wppatend.flow.entities.FlowParameter;
import br.com.wppatend.flow.repositories.FlowInstancePhoneNumberRepository;
import br.com.wppatend.flow.repositories.FlowInstanceRepository;
import br.com.wppatend.flow.repositories.FlowNodeMenuOptionRepository;
import br.com.wppatend.flow.repositories.FlowNodeRepository;
import br.com.wppatend.flow.repositories.FlowParameterRepository;
import br.com.wppatend.flow.repositories.FlowRepository;
import br.com.wppatend.flow.services.FlowService;

@Service
public class FlowServiceImpl implements FlowService {
	
	@Autowired
	private FlowInstanceRepository flowInstanceRepository;
	@Autowired
	private FlowRepository flowRepository;
	@Autowired
	private FlowNodeRepository flowNodeRepository;
	@Autowired
	private FlowNodeMenuOptionRepository flowNodeMenuOptionRepository;
	@Autowired
	private FlowInstancePhoneNumberRepository flowInstancePhoneNumberRepository;
	@Autowired
	private FlowParameterRepository flowParameterRepository;

	@Override
	public Flow saveFlow(Flow flow) {
		return flowRepository.save(flow);
	}

	@Override
	public Optional<Flow> findFlowById(Long id) {
		return flowRepository.findById(id);
	}
	
	@Override
	public Flow loadActiveFlow() {
		List<Flow> flows = flowRepository.findByActive(true); 
		if(flows == null || flows.isEmpty()) {
			return null;
		} else {
			return flows.get(0);
		}
	}

	@Override
	public FlowInstance saveFlowInstance(FlowInstance flowInstance) {
		return flowInstanceRepository.save(flowInstance);
	}

	@Override
	public Optional<FlowInstance> findFlowInstanceById(Long id) {
		return flowInstanceRepository.findById(id);
	}

	@Override
	public FlowNode saveFlowNode(FlowNode flowNode) {
		return flowNodeRepository.save(flowNode);
	}

	@Override
	public Optional<FlowNode> findFlowNodeById(Long id) {
		return flowNodeRepository.findById(id);
	}

	@Override
	public FlowInstancePhoneNumber saveFlowInstancePhoneNumber(FlowInstancePhoneNumber instance) {
		return flowInstancePhoneNumberRepository.save(instance);
	}

	@Override
	public Optional<FlowInstancePhoneNumber> findFlowInstancePhoneNumberByPhoneNumber(String id) {
		return flowInstancePhoneNumberRepository.findById(id);
	}
	
	@Override
	public FlowInstance findFlowInstanceByProtocolo(Protocolo protocolo) {
		return flowInstanceRepository.findByProtocolo(protocolo).get(0);
	}
	
	@Override
	public void deleteFlowIntancePhoneNumber(FlowInstancePhoneNumber flowInstancePhoneNumber) {
		flowInstancePhoneNumberRepository.delete(flowInstancePhoneNumber);
	}
	
	@Override
	public Page<Flow> getFlowList(Integer pageNumber) {
		PageRequest pageRequest =
                PageRequest.of(pageNumber - 1, 15, Sort.Direction.ASC, "id");

        return flowRepository.findAll(pageRequest);
	}
	
	@Override
	public void deleteFlow(Long id) {
		flowRepository.delete(flowRepository.findById(id).get());
	}
	
	@Override
	public FlowNodeMenuOption saveFlowNodeMenuOption(FlowNodeMenuOption menuOption) {
		return flowNodeMenuOptionRepository.save(menuOption);
	}
	
	@Override
	public Optional<FlowNodeMenuOption> findFlowNodeMenuOptionById(Long id) {
		return flowNodeMenuOptionRepository.findById(id);
	};
	
	@Override
	public Optional<FlowParameter> findFlowParameterById(Long id) {
		return flowParameterRepository.findById(id);
	}
	
	@Override
	public FlowParameter saveFlowParameter(FlowParameter flowParameter) {
		return flowParameterRepository.save(flowParameter);
	}
	
	@Override
	public void deleteFlowParameter(Long id) {
		flowParameterRepository.delete(flowParameterRepository.findById(id).get());
	}

}
