package br.com.wppatend.flow.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.services.ParametroService;

public abstract class Collector {

	protected static final Logger logger = LoggerFactory.getLogger(Collector.class);
	protected FlowInstance flowInstance;
	protected MegaBotRestClient megaBotRestClient;
	protected ParametroService parametroService;
	
	protected Collector(FlowInstance flowInstance, MegaBotRestClient megaBotRestClient, 
			ParametroService parametroService) {
		this.flowInstance = flowInstance;
		this.megaBotRestClient = megaBotRestClient;
		this.parametroService = parametroService;
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

	public abstract void doCollect(String collectValues);
	
}
