package br.com.wppatend.flow.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.exceptions.DecisionException;
import br.com.wppatend.flow.services.FlowService;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.services.ProtocoloService;

public abstract class Decision {
	
	protected static final Logger logger = LoggerFactory.getLogger(Decision.class);
	protected FlowService flowService;
	protected FlowInstance flowInstance;
	protected MegaBotRestClient megaBotRestClient;
	protected ParametroService parametroService;
	protected ProtocoloService protocoloService;
	protected PessoaFisicaRestClient pessoaFisicaRestClient;
	protected PessoaJuridicaRestClient pessoaJuridicaRestClient;
	
	protected Decision(
			FlowInstance flowInstance, 
			MegaBotRestClient megaBotRestClient, 
			ParametroService parametroService, 
			ProtocoloService protocoloService,
			PessoaFisicaRestClient pessoaFisicaRestClient, 
			PessoaJuridicaRestClient pessoaJuridicaRestClient,
			FlowService flowService) {
		this.flowService = flowService;
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
	
	protected FlowService getFlowService() {
		return flowService;
	}

	protected void setFlowService(FlowService flowService) {
		this.flowService = flowService;
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
	
	protected void logInfo(String msg) {
		logger.info(msg);
	}
	
	protected void logError(String msg, Throwable t) {
		logger.error(msg, t);
	}

	public abstract void init();
	public abstract boolean isConditionSatisfied() throws DecisionException;

}
