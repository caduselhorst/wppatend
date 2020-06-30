package br.com.wppatend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.wppatend.entities.Finalizacao;
import br.com.wppatend.vos.ITotalizadorFinalizacao;

public interface FinalizacaoRepository extends JpaRepository<Finalizacao, Long> {
	@Query("select b.descricao as finalizacao, count(*) as totalFinalizacao from protocolo a, finalizacao b "
			+ "where a.finalizacao = b.id and date_format(a.dataInicio, '%Y%m%d')= date_format(now(), '%Y%m%d') group by b.descricao")
	public List<ITotalizadorFinalizacao> countTotalFinalizacao();
}
