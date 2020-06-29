package br.com.wppatend.services;

import java.util.List;

import br.com.wppatend.entities.EmpresaContato;

public interface EmpresaContatoService {

	public EmpresaContato save(EmpresaContato empresaContato);
	public void delete (EmpresaContato empresaContato);
	public List<EmpresaContato> carregaPorIdPessoaf(Long pessoaf);
	public List<EmpresaContato> carregaPorIdPessoaj(Long pessoaj);
	
}
