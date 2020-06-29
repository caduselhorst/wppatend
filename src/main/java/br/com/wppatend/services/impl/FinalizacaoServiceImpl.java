package br.com.wppatend.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.Finalizacao;
import br.com.wppatend.repositories.FinalizacaoRepository;
import br.com.wppatend.services.FinalizacaoService;

@Service
public class FinalizacaoServiceImpl implements FinalizacaoService {

	@Autowired
	private FinalizacaoRepository repository;
	
	@Override
	public Optional<Finalizacao> loadById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Finalizacao save(Finalizacao finalizacao) {
		return repository.save(finalizacao);
	}

	@Override
	public Page<Finalizacao> getList(Integer pageNumber) {
		PageRequest pageRequest =
                PageRequest.of(pageNumber - 1, 5, Sort.Direction.ASC, "id");

        return repository.findAll(pageRequest);
	}

	@Override
	public void delete(Finalizacao finalizacao) {
		repository.delete(finalizacao);
	}

	@Override
	public List<Finalizacao> findAll() {
		return repository.findAll();
	}

}
