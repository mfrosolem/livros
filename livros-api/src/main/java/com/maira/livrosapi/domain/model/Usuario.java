package com.maira.livrosapi.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String senha;
	
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime", updatable = false)
	private OffsetDateTime dataCadastro;

	@ManyToOne //eager
	@JoinColumn(name = "grupo_id", nullable = false)
	private Grupo grupo;

	@Column(nullable = false)
	private Boolean primeiroAcesso;

	@Transient
	private String strSenha;
	
	public boolean isNovo() {
		return getId() == null;
	}

	@PrePersist
	private void prePersist() {
		primeiroAcesso = false;
		dataCadastro = OffsetDateTime.now();
	}


}
