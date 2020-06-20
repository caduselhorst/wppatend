package br.com.wppatend.services;

import java.util.List;

import br.com.wppatend.entities.FilaAtentimento;

public interface FilaAtendimentoService {

	public List<FilaAtentimento> findAll();
	public void delete(FilaAtentimento fila);
	
}
