package com.maira.livrosapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Editora {
	
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	private String urlSiteOficial;
	
	private String urlFacebook;
	
	private String urlTwitter;
	
	private String urlWikipedia;

}
