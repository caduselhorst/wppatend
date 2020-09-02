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
import br.com.wppatend.flow.repositories.FlowInstancePhoneNumberRepository;
import br.com.wppatend.flow.repositories.FlowInstanceRepository;
import br.com.wppatend.flow.repositories.FlowNodeRepository;
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
	private FlowInstancePhoneNumberRepository flowInstancePhoneNumberRepository;

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

}
