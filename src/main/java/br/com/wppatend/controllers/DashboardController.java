package br.com.wppatend.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.entities.FilaAtendimento;
import br.com.wppatend.entities.Roteirizador;
import br.com.wppatend.entities.User;
import br.com.wppatend.services.FilaAtendimentoService;
import br.com.wppatend.services.FinalizacaoService;
import br.com.wppatend.services.RoteirizadorService;
import br.com.wppatend.services.UserService;
import br.com.wppatend.vos.DashboardFila;
import br.com.wppatend.vos.DashboardUsuario;
import br.com.wppatend.vos.DashboardVO;
import br.com.wppatend.vos.ITotalizadorFinalizacao;
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
    	
    	List<ITotalizadorFinalizacao> finalizacoes = finalizacaoService.countFinalizacoes();
    	Map<String, Long> map = new HashMap<>();
    	finalizacoes.forEach(f -> {
    		map.put(f.getFinalizacao(), f.getTotalFinalizacao());
    	});
    	
    	
    	DashboardVO vo = new DashboardVO();
    	vo.setFila(lFila);
    	vo.setFinalizacoes(finalizacoes);
    	vo.setUsuarios(usuarios);
    	
    	return ResponseEntity.ok(vo);
	}

}
