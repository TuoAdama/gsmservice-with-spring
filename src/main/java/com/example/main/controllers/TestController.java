package com.example.main.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.entities.Transfert;

@RestController
public class TestController {

	@Value("${syntaxe.transfert}")
	private String syntaxe;
	@Value("${validateurURL}")
	private String validatorUrl;
	
	@GetMapping("/transferts")
	private List<Transfert> testData() {
		
		Transfert transfert1 = Transfert.builder()
				.id(1L)
				.numero("0150388646")
				.montant(50L)
				.build();
		
		return Arrays.asList(transfert1);
	}
	
}
