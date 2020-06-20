package br.com.wppatend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.FilaAtentimento;
import br.com.wppatend.repositories.FilaAtendimentoRepository;
import br.com.wppatend.services.FilaAtendimentoService;

@Service
public class FilaAtendimentoServiceImpl implements FilaAtendimentoService {

	@Autowired
	private FilaAtendimentoRepository repository;
	
	@Override
	public List<FilaAtentimento> findAll() {
		return repository.findAll(Sort.by(Sort.Direction.ASC, "dataFila"));
	}

	@Override
	public void delete(FilaAtentimento fila) {
		repository.delete(fila);
	}

}
