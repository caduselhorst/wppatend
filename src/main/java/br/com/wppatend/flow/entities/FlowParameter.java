package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name="flowparameter")
@SQLDelete(sql = "update flowparameter set deleted = true where id=?")
@Where(clause = "deleted = false")
public class FlowParameter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FlowParamSeq")
	@SequenceGenerator(name = "FlowParamSeq", sequenceName = "flowparamseq", allocationSize = 1)
	private Long id;
	private String name;
	private String classType;
	@Lob
	private byte[] value;
	private boolean deleted;
	
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
	
	public byte[] getValue() {
		return value;
	}
	
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}
	
	
}
