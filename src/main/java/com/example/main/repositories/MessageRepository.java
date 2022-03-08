package com.example.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

}
