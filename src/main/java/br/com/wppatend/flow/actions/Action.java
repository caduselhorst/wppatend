package br.com.wppatend.flow.actions;

import br.com.wppatend.flow.entities.Flow;

public abstract class Action {
	
	protected Flow flow;
	
	protected Action(Flow flow) {
		this.flow = flow;
	}
	
	protected Flow getFlow() {
		return flow;
	}

	protected void setFlow(Flow flow) {
		this.flow = flow;
	}

	public abstract Object doAction(Object[] params);

}
