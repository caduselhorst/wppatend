package br.com.wppatend.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.User;
import br.com.wppatend.repositories.UserRepository;
import br.com.wppatend.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static final int PAGE_SIZE = 5;
	
	@Autowired
    private UserRepository userRepository;
    
	@Override
	public User save(User user) {
		//user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setRoles(new HashSet<>(roleRepository.findAll()));
        return userRepository.save(user);
	}

	@Override
	public User findByUserName(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public Page<User> getList(Integer pageNumber) {
		PageRequest pageRequest =
                PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "id");

        return userRepository.findAll(pageRequest);
	}

	@Override
	public Optional<User> loadById(Long id) {
		return userRepository.findById(id);
	}

}
