package com.example.main.controllers;

import java.util.HashMap;
import java.util.Map;

import com.example.main.entities.Transfert;

public class ValidationController {
	
	public HashMap<String, String> validateTransfert(Transfert transfert) {
		
		HashMap<String, String> errors = new HashMap<>();
		
		if(transfert.getMontant() < 25) {
			errors.put("montant", "Montant inferieur à 25");
		}
		
		if(!transfert.getNumero().matches("^01[0-9]{8}$")) {
			errors.put("numero", "numero invalide");
		}
		
		if(transfert.getId() == null) {
			errors.put("id", "id null");
		}
		
		return errors;
		
	}
	
	public void printErrors(HashMap<String, String> errors ) {
		
		for (Map.Entry<String, String> error : errors.entrySet()) {
			System.out.println(error.getKey() + ":" + error.getValue());
		}
	}
	

}
