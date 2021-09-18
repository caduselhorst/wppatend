package br.com.wppatend.flow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.flow.entities.FlowInstance;

public interface FlowInstanceRepository extends JpaRepository<FlowInstance, Long> {
	public List<FlowInstance> findByProtocolo(Protocolo protocolo);
}
