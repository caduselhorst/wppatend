package br.com.wppatend.flow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.flow.entities.Flow;

public interface FlowRepository extends JpaRepository<Flow, Long> {
	
	public List<Flow> findByActive(boolean active);

}
