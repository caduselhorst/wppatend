package br.com.wppatend.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wppatend.entities.Finalizacao;
import br.com.wppatend.vos.ITotalizadorFinalizacao;


public interface FinalizacaoRepository extends JpaRepository<Finalizacao, Long> {
	
	@Query("select b.descricao as finalizacao, count(*) as totalFinalizacao from protocolo a, finalizacao b "
			+ "where a.finalizacao = b.id and a.dataFechamento >= :dataInicio and a.dataFechamento <= :dataFim group by b.descricao")
	public List<ITotalizadorFinalizacao> countTotalFinalizacao(@Param(value="dataInicio") Date dataInicio, @Param(value="dataFim") Date dataFim );
	
}
