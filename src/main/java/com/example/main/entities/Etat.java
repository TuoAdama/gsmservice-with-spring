package com.example.main.entities;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "transferts")
public class Etat {
	
	@Transient
	public static final String EXECUTE = "EXECUTE";
	@Transient
	public static final String ECHOUE =  "ECHOUE";
	@Transient
	public static final String EN_COURS = "EN COURS";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "etat", cascade = CascadeType.ALL)
	private Set<Transfert> transferts;
	
	@Column(nullable = false, insertable = false, updatable = false, 	columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdAt ;
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updateAt;
}