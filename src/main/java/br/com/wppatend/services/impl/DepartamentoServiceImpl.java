package br.com.wppatend.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.Departamento;
import br.com.wppatend.repositories.DepartamentoRepository;
import br.com.wppatend.services.DepartamentoService;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {
	
	private static final int PAGE_SIZE = 5;
	
	@Autowired
	private DepartamentoRepository repository;

	@Override
	public Optional<Departamento> loadById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Departamento save(Departamento departamento) {
		return repository.save(departamento);
	}

	@Override
	public Page<Departamento> getPaginatedList(Integer pageNumber) {
		PageRequest pageRequest =
                PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "id");

        return repository.findAll(pageRequest);
	}

	@Override
	public List<Departamento> getList() {
		return repository.findAll();
	}

	@Override
	public void delete(Departamento departamento) {
		repository.delete(departamento);
	}
	

}
