package br.com.wppatend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wppatend.entities.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	
	public List<Chat> findByprotocolo(Long protocolo);
	
	@Query("select c from chat c where c.protocolo=:protocolo")
	public Page<Chat> findByprotocolo(@Param("protocolo") Long protocolo,  Pageable page);

}
