package br.com.wppatend.roteirizador;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import br.com.wppatend.entities.FilaAtentimento;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.entities.Roteirizador;
import br.com.wppatend.services.ConfigurationService;
import br.com.wppatend.services.FilaAtendimentoService;
import br.com.wppatend.services.ProtocoloService;
import br.com.wppatend.services.RoteirizadorService;

@Component
@ApplicationScope
public class RoteirizadorThread extends Thread {
	
	
	@Autowired	
	private ConfigurationService configService;
	@Autowired
	private FilaAtendimentoService filaService;
	@Autowired
	private RoteirizadorService roteirizadorService;
	@Autowired
	private ProtocoloService protocoloService;

	
	private static final Logger logger = LoggerFactory.getLogger(RoteirizadorThread.class);
	private boolean parar;
	
	@PostConstruct
	public void init() {
		parar = false;
		logger.info("Solicitação de parada do processo.");
	}
	
	public void finalizar() {
		parar = true;
	}
	
	@Override
	public void run() {
		logger.info("Iniciando o roteirizador");
		while(!parar) {
			List<FilaAtentimento> fila = filaService.findAll();
			if(fila != null) {
				fila.forEach(f -> {
					Optional<Roteirizador> r = roteirizadorService.findByDisponivel();
					if(r.isPresent()) {
						Protocolo p = f.getProtocolo();
						p = protocoloService.findById(p.getId()).get();
						p.setOperador(r.get().getUserId());
						p = protocoloService.save(p);
						filaService.delete(f);
					}
				});
			}
				
			try {
				Thread.sleep(configService.getTempoPoolingRoteirizador());
			} catch (InterruptedException e) {
				logger.warn("Erro de interrução de thread");
			}
		}
		logger.info("Roteirizador finalizado.");
	}
	

}
