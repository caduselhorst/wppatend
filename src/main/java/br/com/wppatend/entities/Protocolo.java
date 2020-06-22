package br.com.wppatend.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "protocolo")
public class Protocolo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProtocoloSeq")
	@SequenceGenerator(name = "ProtocoloSeq", sequenceName = "protocoloseq", allocationSize = 1)
	private Long id;
	private String fone;
	private Long codPessoa;
	private Long codPessoaJuridica;
	private String protocolo;
	@OneToOne
	private EstadoAtendimento estado;
	private Date dataInicio;
	private Date dataAlteracao;
	private Date dataFechamento;
	@Column(columnDefinition = "text")
	private String ultMsgDig;
	private Long operador;
	private Long finalizacao;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFone() {
		return fone;
	}
	public void setFone(String fone) {
		this.fone = fone;
	}
	public Long getCodPessoa() {
		return codPessoa;
	}
	public void setCodPessoa(Long codPessoa) {
		this.codPessoa = codPessoa;
	}
	public Long getCodPessoaJuridica() {
		return codPessoaJuridica;
	}
	public void setCodPessoaJuridica(Long codPessoaJuridica) {
		this.codPessoaJuridica = codPessoaJuridica;
	}
	public String getProtocolo() {
		return protocolo;
	}
	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	public EstadoAtendimento getEstado() {
		return estado;
	}
	public void setEstado(EstadoAtendimento estado) {
		this.estado = estado;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	public Date getDataFechamento() {
		return dataFechamento;
	}
	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}
	public String getUltMsgDig() {
		return ultMsgDig;
	}
	public void setUltMsgDig(String ultMsgDig) {
		this.ultMsgDig = ultMsgDig;
	}
	public Long getOperador() {
		return operador;
	}
	public void setOperador(Long operador) {
		this.operador = operador;
	}
	public Long getFinalizacao() {
		return finalizacao;
	}
	public void setFinalizacao(Long finalizacao) {
		this.finalizacao = finalizacao;
	}
	
}
