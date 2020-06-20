package br.com.wppatend.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.Permission;
import br.com.wppatend.repositories.PermissionRepository;
import br.com.wppatend.services.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

	private static final int PAGE_SIZE = 5;
	
	@Autowired
	private PermissionRepository repository;
	
	@Override
	public Optional<Permission> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Permission save(Permission permission) {
		return repository.save(permission);
	}

	@Override
	public Page<Permission> getList(Integer pageNumber) {
		PageRequest pageRequest =
                PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "id");

        return repository.findAll(pageRequest);

	}
	
	@Override
	public List<Permission> getList() {
		return repository.findAll();
	}

}
