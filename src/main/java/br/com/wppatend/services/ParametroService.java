package br.com.wppatend.services;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import br.com.wppatend.entities.Parametro;

public interface ParametroService {
	
	public List<Parametro> lista();
	public Page<Parametro> getList(Integer pageNumber);
	public Parametro grava(Parametro parametro);
	public Optional<Parametro> carrega(String chave);
		
	public String getMensagemCliente();
	public String getApiUrlPessoaFisica();
	public String getApiUrlPessoaJuridica();
	public String getUrlApiMegaBot();
	public String getTokenApiMegaBot();
	public boolean isApiSendMsg();
	public boolean isModoDesenvolvimento();
	public List<String> getTelefonesDevMode();
	public String getMsgDevMode();
	public String getUrlApiConsultaCNPJ();
	public Long getTempoPoolingRoteirizador();
	public String getMensagemFinalizacaoAtendimento();
	public String getMensagemErro();
	public String getMensagemHorarioAtendimento();
	public boolean isHorarioAtendimento();
	public boolean isFeriado();
	public boolean isFeriado(Calendar c);
	public boolean isTrataCliente();
	
}
