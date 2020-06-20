package br.com.wppatend.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name="tbrole")
@SQLDelete(sql = "UPDATE tbrole set deleted = true where id=?")
@Where(clause = "deleted = false")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RoleSeq")
	@SequenceGenerator(name = "RoleSeq", sequenceName = "roleseq", allocationSize = 1)
	private Long id;
	private String nome;
	@ManyToMany(mappedBy = "roles")
	private List<User> users;
	@ManyToMany
	private List<Permission> permissions;
	private boolean deleted;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Role) {
			Role r = (Role) obj;
			if(id == null && r.getId() != null) {
				return false;
			} else {
				if (id != null && r.getId() == null) {
					return false;
				} else {
					if(id == null && r.getId() == null) {
						return nome.equals(r.getNome());
					} else {
						return id.equals(r.getId());
					}
				}
			}
		}
		return false;
	}
	

}
