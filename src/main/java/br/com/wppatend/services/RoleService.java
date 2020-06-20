package br.com.wppatend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import br.com.wppatend.entities.Role;

public interface RoleService {
	
	public Role save(Role role);
	public Optional<Role> findById(Long id);
	public List<Role> findAll();
	public Page<Role> getList(Integer pageNumber);
	public void delete(Long id);

}
