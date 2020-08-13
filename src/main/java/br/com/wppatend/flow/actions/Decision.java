package br.com.wppatend.flow.actions;

import br.com.wppatend.flow.entities.Flow;

public abstract class Decision {
	
	private Flow flow;
	
	protected Decision(Flow flow) {
		this.flow = flow;
	}

	protected Flow getFlow() {
		return flow;
	}

	protected void setFlow(Flow flow) {
		this.flow = flow;
	}
	
	public abstract boolean isConditionSatisfied(Object[] params);

}
