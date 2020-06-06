package br.com.wppatend.wpprequest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PessoaJuridica {
	
	private Long idpessoaj;
	private String numerowa;
	private String numerocel;
	private String numerofixo;
	private String razaosocial;
	private String fantasia;
	private String cnpj;
	private String ie;
	private String cep;
	private String tipologradouro;
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String email;
	private String data_cadastro;
	private String data_atualizacao;
	private List<EmpresaAtv> atividades;
	
	public Long getIdpessoaj() {
		return idpessoaj;
	}
	public void setIdpessoa_j(Long idpessoaj) {
		this.idpessoaj = idpessoaj;
	}
	public String getNumero_wa() {
		return numerowa;
	}
	public void setNumerowa(String numerowa) {
		this.numerowa = numerowa;
	}
	public String getNumero_cel() {
		return numerocel;
	}
	public void setNumero_cel(String numerocel) {
		this.numerocel = numerocel;
	}
	public String getNumero_fixo() {
		return numerofixo;
	}
	public void setNumerofixo(String numerofixo) {
		this.numerofixo = numerofixo;
	}
	public String getRazaosocial() {
		return razaosocial;
	}
	public void setRazaosocial(String razaosocial) {
		this.razaosocial = razaosocial;
	}
	public String getFantasia() {
		return fantasia;
	}
	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getIe() {
		return ie;
	}
	public void setIe(String ie) {
		this.ie = ie;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getTipologradouro() {
		return tipologradouro;
	}
	public void setTipologradouro(String tipologradouro) {
		this.tipologradouro = tipologradouro;
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
	public String getData_cadastro() {
		return data_cadastro;
	}
	public void setData_cadastro(String data_cadastro) {
		this.data_cadastro = data_cadastro;
	}
	public String getData_atualizacao() {
		return data_atualizacao;
	}
	public void setData_atualizacao(String data_atualizacao) {
		this.data_atualizacao = data_atualizacao;
	}
	public List<EmpresaAtv> getAtividades() {
		return atividades;
	}
	public void setAtividades(List<EmpresaAtv> atividades) {
		this.atividades = atividades;
	}

}