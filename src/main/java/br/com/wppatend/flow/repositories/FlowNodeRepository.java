package br.com.wppatend.flow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowNode;

public interface FlowNodeRepository extends JpaRepository<FlowNode, Long> {

	public List<FlowNode> findByFlowOrderByName(Flow flow);
	
}
