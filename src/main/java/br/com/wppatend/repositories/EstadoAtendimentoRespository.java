package br.com.wppatend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.entities.EstadoAtendimento;

public interface EstadoAtendimentoRespository 
		extends JpaRepository<EstadoAtendimento, Integer> {
	
}
