package br.com.wppatend.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "protocolo")
public class Protocolo {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	
}
