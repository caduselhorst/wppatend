package br.com.wppatend.flow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.entities.FlowInstanceParameter;

public interface FlowInstanceParameterRepository extends JpaRepository<FlowInstanceParameter, Long> {
	
	List<FlowInstanceParameter> findByFlowInstance(FlowInstance flowInstance);

}
