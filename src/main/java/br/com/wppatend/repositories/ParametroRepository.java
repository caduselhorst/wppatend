package br.com.wppatend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.entities.Parametro;

public interface ParametroRepository extends JpaRepository<Parametro, String> {

}
