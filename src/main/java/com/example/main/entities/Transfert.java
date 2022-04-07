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
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transferts")
@ToString(exclude = "message")
public class Transfert {
	
	@Id
	@NotNull(message = "Identifiant non correct")
	@Min(value = 0, message = "Identifiant doit être supérieur à 0")
	private Long id;
	
	@Column(nullable = false)
	@NotNull(message = "Numéro non existant")
	@Pattern(regexp = "^01[0-9]{8}$", message = "Numéro invalide")
	private String numero;
	
	@Column(nullable = false)
	@NotNull
	@Min(value = 50, message = "Montant non valide")
	private Long montant;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="etat_id")
	private Etat etat;
	
	@OneToOne(mappedBy = "transfert", cascade = CascadeType.ALL)
	private Message message;
	
	@NotNull(message = "Syntaxe ne peut être null")
	private String syntaxe;
	
	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdAt ;
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updateAt;

}
