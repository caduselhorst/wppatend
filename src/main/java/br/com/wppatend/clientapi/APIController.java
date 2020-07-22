package br.com.wppatend.clientapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.wppatend.api.model.ApiProtocolo;
import br.com.wppatend.api.model.ApiReturn;
import br.com.wppatend.api.model.EstadoOperadorInfo;
import br.com.wppatend.api.model.LoginInfo;
import br.com.wppatend.api.model.LogoutInfo;
import br.com.wppatend.api.model.ProtocoloInfo;
import br.com.wppatend.api.model.SendBase64FileInfo;
import br.com.wppatend.api.model.SendMessageInfo;
import br.com.wppatend.api.model.UserResponse;
import br.com.wppatend.clients.MegaBotRestClient;
import br.com.wppatend.clients.PessoaFisicaRestClient;
import br.com.wppatend.clients.PessoaJuridicaRestClient;
import br.com.wppatend.constraints.DirecaoMensagem;
import br.com.wppatend.entities.Chat;
import br.com.wppatend.entities.Finalizacao;
import br.com.wppatend.entities.Protocolo;
import br.com.wppatend.entities.Role;
import br.com.wppatend.entities.Roteirizador;
import br.com.wppatend.entities.User;
import br.com.wppatend.services.ChatService;
import br.com.wppatend.services.FinalizacaoService;
import br.com.wppatend.services.ParametroService;
import br.com.wppatend.services.ProtocoloService;
import br.com.wppatend.services.RoteirizadorService;
import br.com.wppatend.services.UserService;
import br.com.wppatend.wpprequest.model.PessoaFisica;
import br.com.wppatend.wpprequest.model.PessoaJuridica;

@RestController
@RequestMapping("/web/api/v1")
public class APIController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoteirizadorService roteirizadorService;
	@Autowired
	private ProtocoloService protocoloService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private FinalizacaoService finalizacaoService;
	@Autowired
	private ParametroService parametroService;
	
	@Autowired
	private PessoaFisicaRestClient pfClient;
	@Autowired
	private PessoaJuridicaRestClient pjClient;
	@Autowired
	private MegaBotRestClient botClient;
	
	
	@PostMapping(path ="/user/login")
	private ResponseEntity<ApiReturn> login (@RequestBody LoginInfo loginInfo) {
		
		User user = userService.findByUserName(loginInfo.getUserName());
		
		if(user == null) {
			ApiReturn ret = new ApiReturn();
			ret.setError(true);
			ret.setMessage("Usuário não conhecido");
			return ResponseEntity.ok(ret);
		}
		
		if(!new BCryptPasswordEncoder().matches(loginInfo.getPassWord(), user.getPassword())) {
			ApiReturn ret = new ApiReturn();
			ret.setError(true);
			ret.setMessage("Senha incorreta");
			return ResponseEntity.ok(ret);
		}
		
		Roteirizador rot = new Roteirizador();
		rot.setDisponivel(false);
		rot.setEmAtendimento(false);
		rot.setUserId(user.getId());
		rot.setData(new Date());
		rot.setNroAtendimentos(0);
		
		roteirizadorService.save(rot);
		
		UserResponse resp = new UserResponse();
		resp.setId(user.getId());
		resp.setUserName(user.getUsername());
		resp.setName(user.getName());
		List<String> roles = new ArrayList<>();
		for(Role r :  user.getRoles()) {
			roles.add(r.getNome());
		}
		resp.setRoles(roles.toArray(new String[] {}));
		
		ApiReturn ret = new ApiReturn();
		ret.setError(false);
		ret.setReturns(resp);
		return ResponseEntity.ok(ret);
	}
	
	@PostMapping(path ="/user/logout")
	private ResponseEntity<ApiReturn> logout (@RequestBody LogoutInfo info) {
				
		roteirizadorService.delete(roteirizadorService.loadById(info.getIdUser()).get());
		
		ApiReturn ret = new ApiReturn();
		ret.setError(false);
		return ResponseEntity.ok(ret);
	}
	
	@PostMapping(path ="/user/estado")
	private ResponseEntity<ApiReturn> setDisponivel (@RequestBody EstadoOperadorInfo info) {
		
		
		Roteirizador rot = roteirizadorService.loadById(info.getIdUser()).get();
		
		rot.setDisponivel(info.getDisponivel());
		rot.setEmAtendimento(info.getEmAtendimento());
		if(rot.isEmAtendimento()) {
			rot.setNroAtendimentos(rot.getNroAtendimentos() + 1);
		}
		
		roteirizadorService.save(rot);
		
		ApiReturn ret = new ApiReturn();
		ret.setError(false);
		return ResponseEntity.ok(ret);
	}
	
	@RequestMapping(value="/protocolo/user/{userid}", method = RequestMethod.GET)
	private ResponseEntity<ApiProtocolo> getProtocolo (@PathVariable("userid") Long userId) {
		
		ApiProtocolo apiProtocolo = null;
		Protocolo p = protocoloService.fingProtocoloAbertoByOperador(userId);
		
		if(p != null) {
			PessoaFisica pf = p.getCodPessoa() != null ? pfClient.getPessoaFisicaById(p.getCodPessoa()) : null;
			PessoaJuridica pj = p.getCodPessoaJuridica() != null ? pjClient.getPessoaJuridicaById(p.getCodPessoaJuridica()) : null;
			
			apiProtocolo = new ApiProtocolo();
			apiProtocolo.setContato(p.getFone());
			apiProtocolo.setId(p.getId());
			apiProtocolo.setNumero(p.getProtocolo());
			apiProtocolo.setPessoaFisica(pf);
			apiProtocolo.setPessoaJuridica(pj);
			apiProtocolo.setDataAbertura(p.getDataInicio());
		}
				
		return ResponseEntity.ok(apiProtocolo);
	}
	
	@GetMapping(path ="/chat/{protocolo}/{pageNumber}/{pageSize}")
	public ResponseEntity<Page<Chat>> getChat(@PathVariable("protocolo") Long protocolo,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		
		Page<Chat> chat = chatService.findChatByProtocoloId(protocolo, pageNumber, pageSize);
				
		return ResponseEntity.ok(chat);
		
	}
	
	@PostMapping(path ="/protocolo")
	private ResponseEntity<ApiReturn> fechaProtocolo (@RequestBody ProtocoloInfo info) {
		
		Protocolo p = protocoloService.findById(info.getIdProtocolo()).get();
		p.setDataFechamento(new Date());
		p.setFinalizacao(info.getIdFinalizacao());
		p = protocoloService.save(p);
		
		botClient.sendMessage(p.getFone(), parametroService.getMensagemFinalizacaoAtendimento());
		
		ApiReturn ret = new ApiReturn();
		ret.setError(false);
		
		return ResponseEntity.ok(ret);
		
	}
	
	@PostMapping(path ="/chat/send")
	private ResponseEntity<ApiReturn> sendMessage (@RequestBody SendMessageInfo info) {
		
		Chat chat = new Chat();
		chat.setData_tx_rx(new Date());
		chat.setTipo("chat");
		chat.setBody(info.getMessage());
		chat.setProtocolo(info.getProtocolo());
		chat.setTx_rx(DirecaoMensagem.ENVIADA);
		
		
		chatService.save(chat);
		
		botClient.sendMessage(info.getPhoneNumber(), info.getMessage());
		
		ApiReturn ret = new ApiReturn();
		ret.setError(false);
		
		return ResponseEntity.ok(ret);
		
	}
	
	@PostMapping(path ="/chat/sendBase64File")
	private ResponseEntity<ApiReturn> sendBase64File (@RequestBody SendBase64FileInfo info) {
		
		Chat chat = new Chat();
		chat.setData_tx_rx(new Date());
		chat.setProtocolo(info.getProtocolo());
		chat.setTx_rx(DirecaoMensagem.ENVIADA);
		chat.setBody(info.getMessage());
		chat.setLegenda(info.getCaption());
		if(info.getFilename().contains(".jpg") || 
				info.getFilename().contains(".jpeg") || 
				info.getFilename().contains(".bmp") ||
				info.getFilename().contains(".ico") ||
				info.getFilename().contains(".png")) {
			chat.setTipo("image");
		} else {
			chat.setTipo("document");
		}
		
		//chat.setMsg_arquivo(info.getMessage());
		chatService.save(chat);
		
		botClient.sendBase64File(info.getPhoneNumber(), info.getMessage(), info.getFilename(), info.getCaption());
		
		ApiReturn ret = new ApiReturn();
		ret.setError(false);
		
		return ResponseEntity.ok(ret);
		
	}
	
	@GetMapping(path ="/finalizacoes")
	public ResponseEntity<List<Finalizacao>> getFinalizacoes() {
		
		return ResponseEntity.ok(finalizacaoService.findAll());
		
	}
	

}
