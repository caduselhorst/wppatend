package br.com.wppatend.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wppatend.entities.Role;
import br.com.wppatend.entities.User;
import br.com.wppatend.repositories.UserRepository;

@Service
public class UserDetailsServicesImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException("Usuário não localizado");
		}
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for(Role r: user.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(r.getNome()));
		}
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
	}
	

}
