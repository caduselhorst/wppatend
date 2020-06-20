package br.com.wppatend.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.repositories.ProtocoloRepository;
import br.com.wppatend.services.ProtocoloService;

@Service
public class ProtocoloServiceImpl implements ProtocoloService {
	
	@Autowired
	private ProtocoloRepository repository;

	@Override
	public Protocolo findProtocoloAbertoByFone(String fone) {
		return repository.findProtocoloAbertoByFone(fone);
	}

	@Override
	public Protocolo fingProtocoloAbertoByOperador(Long operador) {
		return repository.fingProtocoloAbertoByOperador(operador);
	}

	@Override
	public Optional<Protocolo> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Protocolo save(Protocolo protocolo) {
		return repository.save(protocolo);
	}

}
