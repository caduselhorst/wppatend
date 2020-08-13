package br.com.wppatend.flow.actions;

import br.com.wppatend.flow.entities.Flow;

public abstract class Validator {

	protected Flow flow;
	
	protected Validator(Flow flow) {
		this.flow = flow;
	}
	
	public abstract boolean isValid(String param);
	
}
