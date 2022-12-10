package br.com.wppatend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.wppatend.entities.Roteirizador;

public interface RoteirizadorRepository extends JpaRepository<Roteirizador, Long> {
	
	@Query("select r from roteirizador r where r.disponivel=1 and r.emAtendimento=0 order by nroAtendimentos")
	public Optional<Roteirizador> findFirst();
	
	public Optional<Roteirizador> findTopByDisponivelAndEmAtendimentoOrderByNroAtendimentos(boolean disponivel, boolean emAtendimento);
	
}
