package com.example.main.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.main.entities.Solde;
import com.example.main.repositories.SoldeRepository;

@Service
public class SoldeService {

	@Autowired
	SoldeRepository soldeRepository;
	@Lazy
	@Autowired
	GSMService gsmService;

	public void store(Solde solde) {
		soldeRepository.save(solde);
	}

	public boolean updateSolde() {
		
		Long lastId = soldeRepository.lastId();
		Solde currentSolde = gsmService.getSolde();
		currentSolde.setUpdateAt(Timestamp.valueOf(LocalDateTime.now()));
		
		if(lastId == null && currentSolde != null) {
			return soldeRepository.save(currentSolde) != null;
		}
		
		Solde oldSolde = soldeRepository.findById(soldeRepository.lastId()).get();
		
		if(this.soldeIschange(oldSolde, currentSolde)) {
			currentSolde.setUpdateAt(Timestamp.valueOf(LocalDateTime.now()));
			return soldeRepository.save(currentSolde) != null;
		}
		
		return false;
	}
	
	public boolean soldeIschange(Solde previousSolde, Solde currentSolde) {
		return !previousSolde.getSolde().equals(currentSolde.getSolde()) ||
				!previousSolde.getBonus().equals(currentSolde.getBonus()) ;
	}
	
	public List<Solde> findAll(){
		return soldeRepository.findAll();
	}
}
