package br.com.wppatend.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.Parametro;
import br.com.wppatend.repositories.ParametroRepository;
import br.com.wppatend.services.ParametroService;

@Service
public class ParametroServiceImpl implements ParametroService {
	
	@Autowired
	private ParametroRepository repository;

	@Override
	public Parametro grava(Parametro parametro) {
		return repository.save(parametro);
	}

	@Override
	public Optional<Parametro> carrega(String chave) {
		return repository.findById(chave);
	}

	@Override
	public List<Parametro> lista() {
		return repository.findAll();
	}

	@Override
	public Page<Parametro> getList(Integer pageNumber) {
		PageRequest pageRequest =
                PageRequest.of(pageNumber - 1, 15, Sort.Direction.ASC, "chave");

        return repository.findAll(pageRequest);
	}
	
	@Override
	public String getMensagemCliente() {
		return repository.findById("app.msg.cliente").get().getValor();
	}
	
	@Override
	public String getApiUrlPessoaFisica() {
		return repository.findById("api.pessoafisica").get().getValor();
	}
	
	@Override
	public String getApiUrlPessoaJuridica() {
		return repository.findById("api.pessoajuridica").get().getValor();
	}
	
	@Override
	public String getUrlApiMegaBot() {
		return repository.findById("bot.api.url").get().getValor();
	}
	
	@Override
	public String getTokenApiMegaBot() {
		return repository.findById("bot.api.token").get().getValor();
	}
	
	@Override
	public boolean isApiSendMsg() {
		return Boolean.parseBoolean(repository.findById("bot.api.send.msg").get().getValor());
	}
	
	@Override
	public boolean isModoDesenvolvimento() {
		return Boolean.parseBoolean(repository.findById("app.dev.mode").get().getValor());
	}
	
	@Override
	public List<String> getTelefonesDevMode() {
		String[] fones = StringUtils.split(repository.findById("app.dev.mode.phones").get().getValor(), ",");
		ArrayList<String> lista = new ArrayList<>();
		for(String s : fones) {
			lista.add(s);
		}
		return lista;
	}
	
	@Override
	public String getMsgDevMode() {
		return repository.findById("app.dev.mode.msg").get().getValor();
	}
	
	@Override
	public String getUrlApiConsultaCNPJ() {
		return repository.findById("api.consulta.cnpj").get().getValor();
	}
	
	@Override
	public Long getTempoPoolingRoteirizador() {
		return Long.parseLong(repository.findById("roteirizacao.tempo.pooling").get().getValor());
	}
	
	@Override
	public String getMensagemFinalizacaoAtendimento() {
		return repository.findById("app.msg.fim.atendimento").get().getValor();
	}
	
	@Override
	public String getMensagemErro() {
		return repository.findById("bot.api.error.msg").get().getValor();
	}
	

}
