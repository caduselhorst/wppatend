package br.com.wppatend.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

	@Value("${config.app}")
	private String configFilePath;
	
	private Properties properties;
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);
	
	@PostConstruct
	public void init() {
		logger.info("Construindo service ConfigurationService -> " + configFilePath);
		properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(configFilePath)));
		} catch (Exception e) {
			logger.error("Erro ao carregar as configurações", e);
		}
	}
	
	public String getMensagemCliente() {
		return properties.getProperty("app.msg.cliente");
	}
	
	public String getApiUrlPessoaFisica() {
		return properties.getProperty("api.pessoafisica");
	}
	
	public String getApiUrlPessoaJuridica() {
		return properties.getProperty("api.pessoajuridica");
	}
	
	public String getUrlApiMegaBot() {
		return properties.getProperty("bot.api.url");
	}
	
	public String getTokenApiMegaBot() {
		return properties.getProperty("bot.api.token");
	}
	
	public boolean isApiSendMsg() {
		return Boolean.parseBoolean(properties.getProperty("bot.api.send.msg"));
	}
	
	public boolean isModoDesenvolvimento() {
		return Boolean.parseBoolean(properties.getProperty("app.dev.mode"));
	}
	
	public List<String> getTelefonesDevMode() {
		String[] fones = StringUtils.split(properties.getProperty("app.dev.mode.phones"), ",");
		ArrayList<String> lista = new ArrayList<>();
		for(String s : fones) {
			lista.add(s);
		}
		return lista;
	}
	
	public String getMsgDevMode() {
		return properties.getProperty("app.dev.mode.msg");
	}
	
	public String getUrlApiConsultaCNPJ() {
		return properties.getProperty("api.consulta.cnpj");
	}
	
	public Long getTempoPoolingRoteirizador() {
		return Long.parseLong(properties.getProperty("roteirizacao.tempo.pooling"));
	}
	
	public String getMensagemFinalizacaoAtendimento() {
		return properties.getProperty("app.msg.fim.atendimento");
	}
	
	public String getMensagemErro() {
		return properties.getProperty("bot.api.error.msg");
	}
	
}
