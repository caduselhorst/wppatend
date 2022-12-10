package br.com.wppatend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "empresa_contato")
public class EmpresaContato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long pessoaf;
	private Long pessoaj;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPessoaf() {
		return pessoaf;
	}
	public void setPessoaf(Long pessoaf) {
		this.pessoaf = pessoaf;
	}
	public Long getPessoaj() {
		return pessoaj;
	}
	public void setPessoaj(Long pessoaj) {
		this.pessoaj = pessoaj;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EmpresaContato) {
			EmpresaContato sc = (EmpresaContato) obj;
			return (sc.getPessoaf().equals(pessoaf) && sc.getPessoaj().equals(pessoaj));
		}
		return false;
	}

}
