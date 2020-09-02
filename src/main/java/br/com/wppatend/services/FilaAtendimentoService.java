package br.com.wppatend.services;

import java.util.List;

import br.com.wppatend.entities.FilaAtendimento;

public interface FilaAtendimentoService {

	public List<FilaAtendimento> findAll();
	public void delete(FilaAtendimento fila);
	public FilaAtendimento save(FilaAtendimento fila);
	
}
