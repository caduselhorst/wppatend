package br.com.wppatend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
