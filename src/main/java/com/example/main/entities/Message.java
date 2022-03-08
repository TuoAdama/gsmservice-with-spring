package com.example.main.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Transient
	public final static String NO_SUCH_TRANSFERT_RULE= "No such Transfer Rule";
	@Transient
	public final static String TECHNICAL_ERROR = "A technical error has occurred. Please try again later";
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "transfert_id", nullable = false)
	private Transfert transfert;
	
	private String reference;
	
	private String sms;
	
	@Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdAt ;
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updateAt;
}
