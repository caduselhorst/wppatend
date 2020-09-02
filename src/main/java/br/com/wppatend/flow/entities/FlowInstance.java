package br.com.wppatend.flow.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import br.com.wppatend.entities.Protocolo;

@Entity(name = "flowinstance")
public class FlowInstance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FlowInstSeq")
	@SequenceGenerator(name = "FlowInstSeq", sequenceName = "flowinstseq", allocationSize = 1)
	private Long id;
	@OneToOne
	private Flow flow;
	@OneToOne
	private Protocolo protocolo;
	@OneToOne
	private FlowNode actualNode;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<FlowParameter> parameters;
	private Date initialDate;
	private Date updatedDate;
	private Date finishDate;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Flow getFlow() {
		return flow;
	}
	
	public void setFlow(Flow flow) {
		this.flow = flow;
	}
	
	public Protocolo getProtocolo() {
		return protocolo;
	}
	
	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}
	
	public FlowNode getActualNode() {
		return actualNode;
	}
	
	public void setActualNode(FlowNode actualNode) {
		this.actualNode = actualNode;
	}
	
	public Date getInitialDate() {
		return initialDate;
	}
	
	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public List<FlowParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<FlowParameter> parameters) {
		this.parameters = parameters;
	}	

}
