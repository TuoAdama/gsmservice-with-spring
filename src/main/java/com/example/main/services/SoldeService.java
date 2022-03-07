package com.example.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.entities.Solde;
import com.example.main.repositories.SoldeRepository;

@Service
public class SoldeService {

	@Autowired
	SoldeRepository soldeRepository;
	
	
	public void store(Solde solde) {
		soldeRepository.save(solde);
	}
	
	public boolean soldeIsChange(Solde newSolde) {
		Solde oldSolde = soldeRepository.findById(soldeRepository.lastId()).get();
		
		return oldSolde.getBonus() == newSolde.getBonus() && oldSolde.getSolde() == newSolde.getSolde();	
	}
	
	
}
