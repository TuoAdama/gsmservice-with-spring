package com.example.main.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.entities.Etat;
import com.example.main.repositories.EtatRepository;

@Service
public class EtatService {

	@Autowired
	EtatRepository etatRepository;

	public List<Etat> getEtatLoadingOrFailed() {

		List<Etat> etats = new ArrayList<>();
		String[] queries = { "ECHOUE", "EN COURS" };
		for (String query : queries) {
			Etat etat = this.getEtatByName(query);
			if (etat != null) {
				etats.add(etat);
			}
		}

		return etats;
	}

	public Etat getEtatByName(String name) {

		Optional<Etat> optionalEtat = etatRepository.findByName(name);

		if (optionalEtat.isPresent()) {
			return optionalEtat.get();
		}
		return null;
	}

	public List<Etat> findAllEtat() {
		return etatRepository.findAll();
	}
	
	public Etat updateOrSave(Etat etat) {
		return etatRepository.save(etat);
	}

}
