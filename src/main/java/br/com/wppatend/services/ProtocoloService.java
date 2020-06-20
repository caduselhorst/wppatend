package br.com.wppatend.services;

import java.util.Optional;

import br.com.wppatend.entities.Protocolo;

public interface ProtocoloService {
	
	public Optional<Protocolo> findById(Long id);
	public Protocolo findProtocoloAbertoByFone(String fone);	
	public Protocolo fingProtocoloAbertoByOperador(Long operador);
	public Protocolo save(Protocolo protocolo);

}
