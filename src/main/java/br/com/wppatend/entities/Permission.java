package br.com.wppatend.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity(name="tbpermission")
public class Permission {
	
	@Id
	private Long id;
	private String name;
	
	@ManyToMany
	private List<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Permission) {
			Permission p = (Permission) obj;
			if(id == null && p.getId() != null) {
				return false;
			} else {
				if(id != null && p.getId() == null) {
					return false;
				} else {
					return id.equals(p.getId());
				}
			}
		}
		return false;
	}

}
