package com.example.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entities.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long>{
	public Setting findBySettingKey(String name);
}
