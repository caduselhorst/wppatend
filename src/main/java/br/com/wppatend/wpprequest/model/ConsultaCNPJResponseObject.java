package br.com.wppatend.wpprequest.model;

import java.util.List;


public class ConsultaCNPJResponseObject {
	
	
	
	private List<ConsultaCNPJAtivResObj> atividade_principal;
	private String data_situacao;
	private String nome;
	private String uf;
	private String telefone;
	private List<ConsultaCNPJAtivResObj> atividades_secundarias;
	private List<ConsultaCNPJAtivResObj> qsa;
	private String situacao;
	private String bairro;
	private String logradouro;
	private String numero;
	private String cep;
	private String municipio;
	private String porte;
	private String abertura;
	private String natureza_juridica;
	private String fantasia;
	private String cnpj;
	private String status;
	private String tipo;
	private String complemento;
	private String email;
	private String message;
	public List<ConsultaCNPJAtivResObj> getAtividade_principal() {
		return atividade_principal;
	}
	public void setAtividade_principal(List<ConsultaCNPJAtivResObj> atividade_principal) {
		this.atividade_principal = atividade_principal;
	}
	public String getData_situacao() {
		return data_situacao;
	}
	public void setData_situacao(String data_situacao) {
		this.data_situacao = data_situacao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public List<ConsultaCNPJAtivResObj> getAtividades_secundarias() {
		return atividades_secundarias;
	}
	public void setAtividades_secundarias(List<ConsultaCNPJAtivResObj> atividades_secundarias) {
		this.atividades_secundarias = atividades_secundarias;
	}
	public List<ConsultaCNPJAtivResObj> getQsa() {
		return qsa;
	}
	public void setQsa(List<ConsultaCNPJAtivResObj> qsa) {
		this.qsa = qsa;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
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
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getPorte() {
		return porte;
	}
	public void setPorte(String porte) {
		this.porte = porte;
	}
	public String getAbertura() {
		return abertura;
	}
	public void setAbertura(String abertura) {
		this.abertura = abertura;
	}
	public String getNatureza_juridica() {
		return natureza_juridica;
	}
	public void setNatureza_juridica(String natureza_juridica) {
		this.natureza_juridica = natureza_juridica;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
