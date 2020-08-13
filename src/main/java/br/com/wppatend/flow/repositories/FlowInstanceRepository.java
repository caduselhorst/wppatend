package br.com.wppatend.flow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.flow.entities.FlowInstance;

public interface FlowInstanceRepository extends JpaRepository<FlowInstance, Long> {

}
