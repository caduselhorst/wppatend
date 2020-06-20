package br.com.wppatend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
