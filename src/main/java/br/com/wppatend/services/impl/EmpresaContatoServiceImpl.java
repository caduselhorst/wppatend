package br.com.wppatend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.EmpresaContato;
import br.com.wppatend.repositories.EmpresaContatoRepository;
import br.com.wppatend.services.EmpresaContatoService;

@Service
public class EmpresaContatoServiceImpl implements EmpresaContatoService {
	
	@Autowired
	private EmpresaContatoRepository repository;

	@Override
	public EmpresaContato save(EmpresaContato empresaContato) {
		return repository.save(empresaContato);
	}

	@Override
	public void delete(EmpresaContato empresaContato) {
		repository.delete(empresaContato);
	}

	@Override
	public List<EmpresaContato> carregaPorIdPessoaf(Long pessoaf) {
		return repository.findBypessoaf(pessoaf);
	}

	@Override
	public List<EmpresaContato> carregaPorIdPessoaj(Long pessoaj) {
		return repository.findBypessoaj(pessoaj);
	}

}
