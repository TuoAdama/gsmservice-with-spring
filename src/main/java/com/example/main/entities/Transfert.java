package com.example.main.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transfert {
	
	@Id
	private Long id;
	
	@Column(nullable = false)
	@NotNull(message = "Numéro non existant")
	@Pattern(regexp = "^01[0-9]{8}$", message = "Numéro invalide")
	private String numero;
	
	@Column(nullable = false)
	@NotNull
	@Min(value = 25, message = "Montant non valide")
	private Long montant;
	
	private String reference;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Etat etat;
	
	@OneToOne(mappedBy = "transfert", cascade = CascadeType.ALL)
	private Message message;
	
	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdAt ;
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updateAt;

}
