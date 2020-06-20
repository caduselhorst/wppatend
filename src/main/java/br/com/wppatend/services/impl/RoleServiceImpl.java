package br.com.wppatend.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.Role;
import br.com.wppatend.repositories.RoleRepository;
import br.com.wppatend.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	private static final int PAGE_SIZE = 5;
	
	@Autowired
	private RoleRepository repository;

	@Override
	public Role save(Role role) {
		return repository.save(role);
	}

	@Override
	public Optional<Role> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<Role> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<Role> getList(Integer pageNumber) {
		PageRequest pageRequest =
                PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "id");

        return repository.findAll(pageRequest);
	}
	
	@Override
	public void delete(Long id) {
		Optional<Role> role = repository.findById(id);
		repository.delete(role.get());
	}

}
