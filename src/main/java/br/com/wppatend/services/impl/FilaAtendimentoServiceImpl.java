package br.com.wppatend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wppatend.entities.FilaAtendimento;
import br.com.wppatend.repositories.FilaAtendimentoRepository;
import br.com.wppatend.services.FilaAtendimentoService;

@Service
public class FilaAtendimentoServiceImpl implements FilaAtendimentoService {

	@Autowired
	private FilaAtendimentoRepository repository;
	
	@Override
	public List<FilaAtendimento> findAll() {
		return repository.findAll(Sort.by(Sort.Direction.ASC, "dataFila"));
	}

	@Transactional
	@Override
	public void delete(FilaAtendimento fila) {
		repository.delete(fila);
	}
	
	@Transactional
	@Override
	public FilaAtendimento save(FilaAtendimento fila) {
		return repository.save(fila);
	}

}
