package br.com.wppatend.roteirizador;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.wppatend.entities.FilaAtendimento;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.entities.Roteirizador;
import br.com.wppatend.repositories.UserRepository;
import br.com.wppatend.services.FilaAtendimentoService;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.services.ProtocoloService;
import br.com.wppatend.services.RoteirizadorService;

@Component
@Scope("singleton")
public class RoteirizadorThread extends Thread {
	
	
	@Autowired	
	private ParametroService parametroService;
	@Autowired
	private FilaAtendimentoService filaService;
	@Autowired
	private RoteirizadorService roteirizadorService;
	@Autowired
	private ProtocoloService protocoloService;
	@Autowired
	private UserRepository userRepository;
	
	private Thread thread;

	
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
		logger.info("Roteirizador iniciado");
		while(!parar) {
			List<FilaAtendimento> fila = filaService.findAll();
			if(fila != null) {
				fila.forEach(f -> {
					
					Optional<Roteirizador> r = roteirizadorService.findByDisponivel();
					if(r.isPresent() && userRepository.findById(r.get().getUserId()).get().getDepartamentos().contains(f.getDepartamento())) {
						Protocolo p = f.getProtocolo();
						p = protocoloService.findById(p.getId()).get();
						p.setOperador(r.get().getUserId());
						p = protocoloService.save(p);
						filaService.delete(f);
					}
				});
			}
				
			try {
				Thread.sleep(parametroService.getTempoPoolingRoteirizador());
			} catch (InterruptedException e) {
				logger.warn("Erro de interrução de thread");
			}
		}
		logger.info("Roteirizador finalizado.");
		parar = false;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public void setThread(Thread t) {
		thread = t;
	}

}
