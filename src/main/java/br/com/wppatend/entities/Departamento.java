package br.com.wppatend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "departamento")
@SQLDelete(sql = "UPDATE departamento set deleted = true where id=?")
@Where(clause = "deleted = false")
public class Departamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DepartamentoSeq")
	@SequenceGenerator(name = "DepartamentoSeq", sequenceName = "deparamentoseq", allocationSize = 1)
	private Long id;
	private String descricao;
	private boolean deleted;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Departamento) {
			Departamento r = (Departamento) obj;
			if(id == null && r.getId() != null) {
				return false;
			} else {
				if (id != null && r.getId() == null) {
					return false;
				} else {
					if(id == null && r.getId() == null) {
						return descricao.equals(r.getDescricao());
					} else {
						return id.equals(r.getId());
					}
				}
			}
		}
		return false;
	}

}
