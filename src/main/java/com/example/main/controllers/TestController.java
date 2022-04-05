package com.example.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.services.GSMService;
import com.example.main.utils.Config;

@RestController
public class TestController {

	@Autowired Config config;
	@Autowired GSMService gsmService;
	
	@GetMapping("/config")
	private @ResponseBody Config testData() {
		return config;
	}
	
}
