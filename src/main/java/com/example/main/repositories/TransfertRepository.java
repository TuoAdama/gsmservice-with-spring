package com.example.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entities.Transfert;

@Repository
public interface TransfertRepository extends JpaRepository<Transfert, Long>{

}