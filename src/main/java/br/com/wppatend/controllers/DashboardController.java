package br.com.wppatend.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.entities.FilaAtendimento;
import br.com.wppatend.entities.Roteirizador;
import br.com.wppatend.entities.User;
import br.com.wppatend.roteirizador.RoteirizadorThread;
import br.com.wppatend.services.FilaAtendimentoService;
import br.com.wppatend.services.FinalizacaoService;
import br.com.wppatend.services.RoteirizadorService;
import br.com.wppatend.services.UserService;
import br.com.wppatend.vos.DashboardApiStatus;
import br.com.wppatend.vos.DashboardFila;
import br.com.wppatend.vos.DashboardProcesso;
import br.com.wppatend.vos.DashboardUsuario;
import br.com.wppatend.vos.DashboardVO;
import br.com.wppatend.vos.ITotalizadorFinalizacao;
import br.com.wppatend.wpprequest.model.ApiStatus;
import br.com.wppatend.wpprequest.model.PessoaFisica;

@RestController
public class DashboardController {
	
	@Autowired
	private RoteirizadorService roteirizadorService;
	@Autowired
	private UserService userService;
	@Autowired
	private FilaAtendimentoService filaAtendimentoService;
	@Autowired
	private PessoaFisicaRestClient pessoaFisicaRestClient;
	@Autowired
	private FinalizacaoService finalizacaoService;
	@Autowired
	private RoteirizadorThread roteirizador;
	@Autowired
	private MegaBotRestClient megaBotResClient;
	
	@RequestMapping(value = "/web/dashboard", method = RequestMethod.GET)
	public ResponseEntity<DashboardVO> getDashboard() {
		
		List<Roteirizador> roteirizador = roteirizadorService.listAll();
    	
    	List<DashboardUsuario> usuarios = new ArrayList<>();
    	
    	roteirizador.forEach(r -> {
    		User u = userService.loadById(r.getUserId()).get();
    		u.setRoles(null);
    		u.setPassword(null);
    		DashboardUsuario du = new DashboardUsuario();
    		du.setUser(u);
    		du.setEmAtendimento(r.isEmAtendimento());
    		du.setDisponivel(r.isDisponivel());
    		du.setNroAtendimentos(r.getNroAtendimentos());
    		usuarios.add(du);
    	});
    	
    	List<FilaAtendimento> fila =  filaAtendimentoService.findAll();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	List<DashboardFila> lFila = new ArrayList<>();
    	fila.forEach(f -> {
    		
    		PessoaFisica pf = pessoaFisicaRestClient.getPessoaFisicaById(f.getProtocolo().getCodPessoa());
    		
    		DashboardFila df = new DashboardFila();
    		df.setData(sdf.format(f.getDataFila()));
    		df.setFoneContato(f.getProtocolo().getFone());
    		df.setNomeContato(pf.getNome());
    		df.setNroProtocolo(f.getProtocolo().getProtocolo());
    		lFila.add(df);
    	});
    	
    	List<ITotalizadorFinalizacao> finalizacoes = getFinalizacoes();
    	List<ITotalizadorFinalizacao> finalizacoesOntem =  getFinalizacoesOntem();
    	List<ITotalizadorFinalizacao> finalizacoesMes = getFinalizacoesMesAtual();
    	List<ITotalizadorFinalizacao> finalizacoesMesAnterior = getFinalizacoesMesAnterior();
    	List<ITotalizadorFinalizacao> finalizacoesAnoAtual = getFinalizacoesAnoAtual();
    	List<ITotalizadorFinalizacao> finalizacoesAnoAnterior = getFinalizacoesAnoAnterior();
    	
    	DashboardProcesso procRoteirizador = new DashboardProcesso();
    	procRoteirizador.setProcesso("Roteirizador");
    	procRoteirizador.setAtivo(this.roteirizador.getThread() == null ? false : this.roteirizador.getThread().isAlive());
    	
    	List<DashboardProcesso> processos = new ArrayList<>();
    	processos.add(procRoteirizador);
    	
    	
    	ApiStatus apiStatus = megaBotResClient.getApiStatus();
    	DashboardApiStatus dashApiStatus = new DashboardApiStatus();
    	dashApiStatus.setFone(apiStatus.getPhone());
    	dashApiStatus.setMensagem(apiStatus.getMessage());
    	dashApiStatus.setWebhook(apiStatus.getWebhook());
    	dashApiStatus.setStatus(Boolean.parseBoolean(apiStatus.getAccountStatus()));
    	
    	
    	/*
    	DashboardApiStatus dashApiStatus = new DashboardApiStatus();
    	dashApiStatus.setFone(null);
    	dashApiStatus.setMensagem("Não conectado");
    	dashApiStatus.setWebhook(null);
    	dashApiStatus.setStatus(false);
    	*/
    	DashboardVO vo = new DashboardVO();
    	vo.setFila(lFila);
    	vo.setFinalizacoes(finalizacoes);
    	vo.setFinalizacoesAnoAnterior(finalizacoesAnoAnterior);
    	vo.setFinalizacoesAnoAtual(finalizacoesAnoAtual);
    	vo.setFinalizacoesMesAnterior(finalizacoesMesAnterior);
    	vo.setFinalizacoesMesAtual(finalizacoesMes);
    	vo.setFinalizacoesOntem(finalizacoesOntem);
    	
    	vo.setUsuarios(usuarios);
    	vo.setProcessos(processos);
    	vo.setStatus(dashApiStatus);
    	
    	return ResponseEntity.ok(vo);
	}
	
	private List<ITotalizadorFinalizacao> getFinalizacoes() {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		
		
		c2.set(Calendar.HOUR_OF_DAY, 23);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.SECOND, 59);
		
		
		return finalizacaoService.countFinalizacoes(new Date(c1.getTimeInMillis()), new Date(c2.getTimeInMillis()));
	} 
	
	private List<ITotalizadorFinalizacao> getFinalizacoesOntem() {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.add(Calendar.DAY_OF_MONTH, -1);
		
		c2.set(Calendar.HOUR_OF_DAY, 23);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.SECOND, 59);
		c2.add(Calendar.DAY_OF_MONTH, -1);
		
		return finalizacaoService.countFinalizacoes(new Date(c1.getTimeInMillis()), new Date(c2.getTimeInMillis()));
	}
	
	private List<ITotalizadorFinalizacao> getFinalizacoesMesAtual() {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		
		c2.set(Calendar.HOUR_OF_DAY, 23);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.SECOND, 59);
		c2.set(Calendar.DAY_OF_MONTH, c2.getMaximum(Calendar.DAY_OF_MONTH));
		
		return finalizacaoService.countFinalizacoes(new Date(c1.getTimeInMillis()), new Date(c2.getTimeInMillis()));
	}
	
	private List<ITotalizadorFinalizacao> getFinalizacoesMesAnterior() {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.add(Calendar.MONTH, -1);
		
		c2.set(Calendar.HOUR_OF_DAY, 23);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.SECOND, 59);
		c2.add(Calendar.MONTH, -1);
		c2.set(Calendar.DAY_OF_MONTH, c2.getMaximum(Calendar.DAY_OF_MONTH));
		
		return finalizacaoService.countFinalizacoes(new Date(c1.getTimeInMillis()), new Date(c2.getTimeInMillis()));
	}
	
	private List<ITotalizadorFinalizacao> getFinalizacoesAnoAtual() {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		
		c2.set(Calendar.HOUR_OF_DAY, 23);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.SECOND, 59);
		c2.set(Calendar.DAY_OF_MONTH, 31);
		c2.set(Calendar.MONTH, Calendar.DECEMBER);
		
		return finalizacaoService.countFinalizacoes(new Date(c1.getTimeInMillis()), new Date(c2.getTimeInMillis()));
	}
	
	private List<ITotalizadorFinalizacao> getFinalizacoesAnoAnterior() {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		c1.add(Calendar.YEAR, -1);
		
		c2.set(Calendar.HOUR_OF_DAY, 23);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.SECOND, 59);
		c2.set(Calendar.DAY_OF_MONTH, 31);
		c2.set(Calendar.MONTH, Calendar.DECEMBER);
		c2.add(Calendar.YEAR, -1);
		
		return finalizacaoService.countFinalizacoes(new Date(c1.getTimeInMillis()), new Date(c2.getTimeInMillis()));
	}

}
