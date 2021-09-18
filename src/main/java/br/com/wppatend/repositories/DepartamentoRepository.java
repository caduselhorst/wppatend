package br.com.wppatend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.entities.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

}
