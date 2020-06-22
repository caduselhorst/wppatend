package br.com.wppatend.wpprequest.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PessoaFisica {

	private Long idpessoaf;
	private String numerowa;
	private String numerocel;
	private String nome;
	private String cpf;
	private String rg;
	private String cep;
	private String tipoLogradouro;
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String email;
	private Date dataCadastro;
	private Date dataAtualizacao;
	private boolean desejaReceberOfertas;
	public Long getIdpessoaf() {
		return idpessoaf;
	}
	public void setIdpessoaf(Long idpessoaf) {
		this.idpessoaf = idpessoaf;
	}
	public String getNumerowa() {
		return numerowa;
	}
	public void setNumerowa(String numerowa) {
		this.numerowa = numerowa;
	}
	public String getNumerocel() {
		return numerocel;
	}
	public void setNumerocel(String numerocel) {
		this.numerocel = numerocel;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getRg() {
		return rg;
	}
	public void setRg(String rg) {
		this.rg = rg;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getTipoLogradouro() {
		return tipoLogradouro;
	}
	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}
	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	public boolean isDesejaReceberOfertas() {
		return desejaReceberOfertas;
	}
	public void setDesejaReceberOfertas(boolean desejaReceberOfertas) {
		this.desejaReceberOfertas = desejaReceberOfertas;
	}
	
}
