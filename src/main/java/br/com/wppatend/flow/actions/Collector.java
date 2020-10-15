package br.com.wppatend.flow.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.services.ProtocoloService;

public abstract class Collector {

	protected static final Logger logger = LoggerFactory.getLogger(Collector.class);
	protected FlowInstance flowInstance;
	protected MegaBotRestClient megaBotRestClient;
	protected ParametroService parametroService;
	protected ProtocoloService protocoloService;
	protected PessoaFisicaRestClient pessoaFisicaRestClient;
	protected PessoaJuridicaRestClient pessoaJuridicaRestClient;
	
	protected Collector(FlowInstance flowInstance, MegaBotRestClient megaBotRestClient, 
			ParametroService parametroService, ProtocoloService protocoloService,
			PessoaFisicaRestClient pessoaFisicaRestClient, PessoaJuridicaRestClient pessoaJuridicaRestClient) {
		this.flowInstance = flowInstance;
		this.megaBotRestClient = megaBotRestClient;
		this.parametroService = parametroService;
		this.protocoloService = protocoloService;
		this.pessoaFisicaRestClient = pessoaFisicaRestClient;
		this.pessoaJuridicaRestClient = pessoaJuridicaRestClient;
	}
	
	public FlowInstance getFlowInstance() {
		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {
		this.flowInstance = flowInstance;
	}

	protected MegaBotRestClient getMegaBotRestClient() {
		return megaBotRestClient;
	}

	protected void setMegaBotRestClient(MegaBotRestClient megaBotRestClient) {
		this.megaBotRestClient = megaBotRestClient;
	}
	
	protected ParametroService getParametroService() {
		return parametroService;
	}

	protected void setParametroService(ParametroService parametroService) {
		this.parametroService = parametroService;
	}
	
	protected ProtocoloService getProtocoloService() {
		return protocoloService;
	}

	protected void setProtocoloService(ProtocoloService protocoloService) {
		this.protocoloService = protocoloService;
	}
	
	protected PessoaFisicaRestClient getPessoaFisicaRestClient() {
		return pessoaFisicaRestClient;
	}

	protected void setPessoaFisicaRestClient(PessoaFisicaRestClient pessoaFisicaRestClient) {
		this.pessoaFisicaRestClient = pessoaFisicaRestClient;
	}

	protected PessoaJuridicaRestClient getPessoaJuridicaRestClient() {
		return pessoaJuridicaRestClient;
	}

	protected void setPessoaJuridicaRestClient(PessoaJuridicaRestClient pessoaJuridicaRestClient) {
		this.pessoaJuridicaRestClient = pessoaJuridicaRestClient;
	}

	public abstract void doCollect(String collectValues);
	
}
