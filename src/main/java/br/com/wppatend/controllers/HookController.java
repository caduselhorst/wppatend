package br.com.wppatend.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HookController {
	
	@RequestMapping(value = "/hook", method = RequestMethod.POST)
	public String hook (@RequestBody String msg) {
		System.out.println(msg);
		return "OK";
	}

}
