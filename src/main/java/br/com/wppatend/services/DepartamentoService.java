package br.com.wppatend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import br.com.wppatend.entities.Departamento;

public interface DepartamentoService {
	
	public Optional<Departamento> loadById(Long id);
	public Departamento save(Departamento departamento);
	public Page<Departamento> getPaginatedList(Integer pageNumber);
	public List<Departamento> getList();
	public void delete(Departamento departamento);

}
