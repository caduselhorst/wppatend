package br.com.wppatend.services;


import java.util.List;
import java.util.Optional;

import br.com.wppatend.entities.Roteirizador;

public interface RoteirizadorService {
	
	public Roteirizador save(Roteirizador roteirizador);
	public Optional<Roteirizador> loadById(Long id);
	public Optional<Roteirizador> findByDisponivel();
	public void delete(Roteirizador roteirizador);
	public List<Roteirizador> listAll();

}
