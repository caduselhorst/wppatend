package br.com.wppatend.services.impl;

import java.util.ArrayList;

import java.util.Calendar;
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
	
	@Override
	public String getMensagemHorarioAtendimento() {
		return repository.findById("app.horario.atendimento.msg").get().getValor();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean isHorarioAtendimento() {
		Calendar calendar = Calendar.getInstance();
		
		String[] periodos = getSplitedPeriods(calendar.get(Calendar.DAY_OF_WEEK));
		if(periodos == null) {
			return false;
		} else {
			for(int i = 0; i < periodos.length; i++) {
				String[] per = StringUtils.split(periodos[i], "-");
				Calendar c1 = Calendar.getInstance();
				c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(StringUtils.split(per[0], ":")[0]));
				c1.set(Calendar.MINUTE, Integer.parseInt(StringUtils.split(per[0], ":")[1]));
				c1.set(Calendar.SECOND, 0);
				c1.set(Calendar.MILLISECOND, 0);
				Calendar c2 = Calendar.getInstance();
				c2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(StringUtils.split(per[1], ":")[0]));
				c2.set(Calendar.MINUTE, Integer.parseInt(StringUtils.split(per[1], ":")[1]));
				c2.set(Calendar.SECOND, 0);
				c2.set(Calendar.MILLISECOND, 0);
				
				if(calendar.getTimeInMillis() >= c1.getTimeInMillis() && calendar.getTimeInMillis() <= c2.getTimeInMillis()) {
					return true;
				}
				
			}
			return false;
		}
		
		
	}
	
	@Override
	public boolean isFeriado() {
		Calendar c = Calendar.getInstance();
		return isFeriado(c);
	}
	
	@Override
	public boolean isFeriado(Calendar c) {
		String[] feriados = StringUtils.split(repository.findById("app.feriados").get().getValor(), ",");
		for (int i = 0; i < feriados.length; i++) {
			String[] parsed = StringUtils.split(feriados[i], "/");
			if(c.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(parsed[0])
					&& (c.get(Calendar.MONTH) + 1) == Integer.parseInt(parsed[1])) {
				return true;
			}
		}
		return false;
	}
	
	private String[] getSplitedPeriods(int dayOfWeek) {
		switch(dayOfWeek) {
			case Calendar.SUNDAY: {
				String periodo = "";
				if(isFeriado()) {
					periodo = repository.findById("app.horario.atendimento.fer").get().getValor();
				} else {
					periodo = repository.findById("app.horario.atendimento.dom").get().getValor();
				}
				
				if(periodo != null && !periodo.isEmpty()) {
					return StringUtils.split(periodo, ",");
				} else {
					return null;
				}
				
			}
			case Calendar.MONDAY: {
				String periodo = "";
				if(isFeriado()) {
					periodo = repository.findById("app.horario.atendimento.fer").get().getValor();
				} else {
					periodo = repository.findById("app.horario.atendimento.seg").get().getValor();
				}
				
				if(periodo != null && !periodo.isEmpty()) {
					return StringUtils.split(periodo, ",");
				} else {
					return null;
				}
			}
			case Calendar.TUESDAY: {
				String periodo = "";
				if(isFeriado()) {
					periodo = repository.findById("app.horario.atendimento.fer").get().getValor();
				} else {
					periodo = repository.findById("app.horario.atendimento.ter").get().getValor();
				}
				
				if(periodo != null && !periodo.isEmpty()) {
					return StringUtils.split(periodo, ",");
				} else {
					return null;
				}
			}
			case Calendar.WEDNESDAY: {
				String periodo = "";
				if(isFeriado()) {
					periodo = repository.findById("app.horario.atendimento.fer").get().getValor();
				} else {
					periodo = repository.findById("app.horario.atendimento.qua").get().getValor();
				}
				
				if(periodo != null && !periodo.isEmpty()) {
					return StringUtils.split(periodo, ",");
				} else {
					return null;
				}
			}
			case Calendar.THURSDAY: {
				String periodo = "";
				if(isFeriado()) {
					periodo = repository.findById("app.horario.atendimento.fer").get().getValor();
				} else {
					periodo = repository.findById("app.horario.atendimento.qui").get().getValor();
				}
				
				if(periodo != null && !periodo.isEmpty()) {
					return StringUtils.split(periodo, ",");
				} else {
					return null;
				}
			}
			case Calendar.FRIDAY: {
				String periodo = "";
				if(isFeriado()) {
					periodo = repository.findById("app.horario.atendimento.fer").get().getValor();
				} else {
					periodo = repository.findById("app.horario.atendimento.sex").get().getValor();
				}
				
				if(periodo != null && !periodo.isEmpty()) {
					return StringUtils.split(periodo, ",");
				} else {
					return null;
				}
			}
			default: {
				String periodo = "";
				if(isFeriado()) {
					periodo = repository.findById("app.horario.atendimento.fer").get().getValor();
				} else {
					periodo = repository.findById("app.horario.atendimento.sab").get().getValor();
				}
				
				if(periodo != null && !periodo.isEmpty()) {
					return StringUtils.split(periodo, ",");
				} else {
					return null;
				}
			}
		}
	}
	
}
