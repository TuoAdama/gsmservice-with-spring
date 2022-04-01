package com.example.main.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "settings")
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull(message = "veuillez preciser la clé du paramètre")
	@Column(name = "setting_key", unique = true)
	private String settingKey;
	
	@NotNull(message = "L'attribut display_name ne peut être null")
	@Column(name = "display_name", unique = true)
	private String displayName;
	
	@NotNull(message = "La valeur du paramètre ne peut être null")
	private String value;
	
	private String details;
	
	@Column(name = "created_at", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdAt;
	
	@Column(name="updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedAt;
}
