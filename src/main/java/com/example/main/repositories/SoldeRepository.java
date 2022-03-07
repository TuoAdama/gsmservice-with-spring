package com.example.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.main.entities.Solde;

@Repository
public interface SoldeRepository extends JpaRepository<Solde, Long>{
	@Query(value = "SELECT max(u.id) FROM solde u", nativeQuery = true)
	public Long lastId();
}
