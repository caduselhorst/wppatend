package br.com.wppatend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.wppatend.wpprequest.model.WppObjectRequest;

@RestController
public class HookController {
	
	private static final Logger logger = LoggerFactory.getLogger(HookController.class);
	
	@RequestMapping(value = "/hook", method = RequestMethod.POST)
	public void hook (@RequestBody WppObjectRequest msg) {
		logger.info(String.format("Mensagem de: %1$s (%2$s) fromMe: %3$s", msg.getMessages().get(0).getChatName(), msg.getMessages().get(0).getAuthor()));
	}

}
