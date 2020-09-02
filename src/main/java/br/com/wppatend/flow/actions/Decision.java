package br.com.wppatend.flow.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.services.FlowService;
import br.com.wppatend.services.ParametroService;

public abstract class Decision {
	
	protected static final Logger logger = LoggerFactory.getLogger(Decision.class);
	protected FlowInstance flowInstance;
	protected MegaBotRestClient megaBotRestClient;
	protected ParametroService parametroService;
	protected FlowService flowService;
	
	protected Decision(FlowInstance flowInstance, MegaBotRestClient megaBotRestClient, 
			ParametroService parametroService, FlowService flowService) {
		this.flowInstance = flowInstance;
		this.megaBotRestClient = megaBotRestClient;
		this.parametroService = parametroService;
		this.flowService = flowService;
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
	
	public FlowService getFlowService() {
		return flowService;
	}

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}

	public abstract void init();
	public abstract boolean isConditionSatisfied();

}
