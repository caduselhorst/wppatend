package br.com.wppatend.flow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.flow.entities.FlowNode;

public interface FlowNodeRepository extends JpaRepository<FlowNode, Long> {

}
