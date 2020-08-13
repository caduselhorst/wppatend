package br.com.wppatend.flow;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowInstance;
import br.com.wppatend.flow.entities.FlowInstancePhoneNumber;
import br.com.wppatend.flow.services.FlowService;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.wpprequest.model.WppObjectRequest;

@Component
@Scope("singleton")
public class FlowEngine {

	@Autowired
	private FlowService flowService;
	@Autowired
	private MegaBotRestClient megaBotRestClient;
	@Autowired
	private ParametroService parametroService;
	@Autowired
	
	public void handleMessage(WppObjectRequest wppObjectRequest) {
		
		Optional<FlowInstancePhoneNumber> instance = flowService.findFlowInstancePhoneNumberByPhoneNumber(wppObjectRequest.getMessages().get(0).getAuthor());
		
		if(instance.isPresent()) {
			/*
			 * Encontrou vinculo de instancia de flow com o numero de telefone
			 */
		} else {
			Flow f = flowService.loadActiveFlow();
			if(f == null) {
				/*
				 * Não encontrou um flow ativo
				 */
			} else {
				
				FlowInstance fInst = new FlowInstance();
				fInst.setFlow(flow);
				fInst.setInitialDate(new Date());
				fInst.set
				
				FlowInstancePhoneNumber fipn = new FlowInstancePhoneNumber();
				fipn.setPhoneNumber(wppObjectRequest.getMessages().get(0).getAuthor());
				fipn.set
			}
		}
		
	}
	
}
