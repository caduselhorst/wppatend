package br.com.wppatend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wppatend.entities.Protocolo;

public interface ProtocoloRepository extends JpaRepository<Protocolo, Long> {
	@Query("select p from protocolo p where p.fone=:fone and p.dataFechamento is null")
	public Protocolo findProtocoloAbertoByFone(@Param("fone") String fone);
	
	@Query("select p from protocolo p where p.operador=:operador and p.dataFechamento is null")
	public Protocolo fingProtocoloAbertoByOperador(@Param("operador") Long operador);
}
