package br.com.wppatend.services;

import java.util.Optional;

import org.springframework.data.domain.Page;

import br.com.wppatend.entities.User;

public interface UserService {
	
	public Optional<User> loadById(Long id);
	public User save(User user);
	public User findByUserName(String username);
	public Page<User> getList(Integer pageNumber);
	public void delete(User user);

}
