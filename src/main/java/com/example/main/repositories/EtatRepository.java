package com.example.main.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entities.Etat;

@Repository
public interface EtatRepository extends JpaRepository<Etat, Long>{
	public Optional<Etat> findByName(String name);
}
