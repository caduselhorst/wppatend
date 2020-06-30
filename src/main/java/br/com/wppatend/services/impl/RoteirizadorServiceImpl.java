package br.com.wppatend.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.Roteirizador;
import br.com.wppatend.repositories.RoteirizadorRepository;
import br.com.wppatend.services.RoteirizadorService;

@Service
public class RoteirizadorServiceImpl implements RoteirizadorService {

	@Autowired
	private RoteirizadorRepository repository;
	
	@Override
	public Roteirizador save(Roteirizador roteirizador) {
		return repository.save(roteirizador);
	}

	@Override
	public Optional<Roteirizador> loadById(Long id) {
		return repository.findById(id);
	}

	@Override
	public void delete(Roteirizador roteirizador) {
		repository.delete(roteirizador);
	}

	@Override
	public Optional<Roteirizador> findByDisponivel() {
		return repository.findFirst();
	}
	
	@Override
	public List<Roteirizador> listAll() {
		return repository.findAll();
	}

}
