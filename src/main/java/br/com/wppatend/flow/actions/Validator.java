package br.com.wppatend.flow.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.wppatend.flow.entities.FlowInstance;

public abstract class Validator {

	protected static final Logger logger = LoggerFactory.getLogger(Action.class);
	protected FlowInstance flowInstance;
	
	protected Validator(FlowInstance flowInstance) {
		this.flowInstance = flowInstance;
	}
	
	public abstract void init();
	public abstract boolean isValid();
	
}
