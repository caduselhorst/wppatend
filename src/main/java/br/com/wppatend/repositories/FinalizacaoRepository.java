package br.com.wppatend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.entities.Finalizacao;

public interface FinalizacaoRepository extends JpaRepository<Finalizacao, Long> {

}
