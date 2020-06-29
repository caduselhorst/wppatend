package br.com.wppatend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.entities.EmpresaContato;

public interface EmpresaContatoRepository extends JpaRepository<EmpresaContato, Long> {
	
	public List<EmpresaContato> findBypessoaf(Long pessoaf);
	public List<EmpresaContato> findBypessoaj(Long pessoaj);

}
