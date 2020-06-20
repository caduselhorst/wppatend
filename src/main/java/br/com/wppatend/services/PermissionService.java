package br.com.wppatend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import br.com.wppatend.entities.Permission;

public interface PermissionService {

	public Optional<Permission> findById(Long id);
	public Permission save(Permission permission);
	public Page<Permission> getList(Integer pageNumber);
	public List<Permission> getList();
	
}
