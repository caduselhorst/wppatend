package br.com.wppatend.controllers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.wppatend.services.ConfigurationService;
import br.com.wppatend.wpprequest.model.WppObjectRequest;

@RestController
public class MsgRecoveryController {
	
	private static final Logger logger = LoggerFactory.getLogger(MsgRecoveryController.class);
	
	@Autowired
	private ConfigurationService configurationService;
	
	@RequestMapping(value = "/web/recovery", method = RequestMethod.POST)
	public ResponseEntity<String> hook (@RequestBody WppObjectRequest msg) {
		
		boolean devMode = configurationService.isModoDesenvolvimento();
		List<String> fones = configurationService.getTelefonesDevMode();
		
		String phoneAuthor = StringUtils.split(msg.getMessages().get(0).getAuthor(), "@")[0];
		
		if((devMode && fones.contains(phoneAuthor)) || !devMode) {
			logger.info(msg.getMessages().get(0).getBody());
			logger.info(msg.toString());
		}
		
		return ResponseEntity.ok("Ok");
		
	}

}
