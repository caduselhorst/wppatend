package br.com.wppatend.flow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowParameter;

public interface FlowParameterRepository extends JpaRepository<FlowParameter, Long> {
	public List<FlowParameter> findByFlow(Flow flow);
}
