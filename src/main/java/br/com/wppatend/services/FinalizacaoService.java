package br.com.wppatend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import br.com.wppatend.entities.Finalizacao;
import br.com.wppatend.vos.ITotalizadorFinalizacao;

public interface FinalizacaoService {
	
	public Optional<Finalizacao> loadById(Long id);
	public Finalizacao save(Finalizacao finalizacao);
	public Page<Finalizacao> getList(Integer pageNumber);
	public List<Finalizacao> findAll();
	public void delete(Finalizacao finalizacao);
	public List<ITotalizadorFinalizacao> countFinalizacoes();

}
