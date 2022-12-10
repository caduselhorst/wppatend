package br.com.wppatend.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name="tbuser")
@SQLDelete(sql = "UPDATE tbuser set deleted = true where id=?")
@Where(clause = "deleted = false")
public class User {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String name;
	private String password;
	@Transient
	private String passwordConfirm;
	@ManyToMany
	private List<Role> roles;
	@ManyToMany
	private List<Departamento> departamentos;
	private boolean deleted;
	
}
