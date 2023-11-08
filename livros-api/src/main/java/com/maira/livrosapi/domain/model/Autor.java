package com.maira.livrosapi.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "autor", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"nome", "sobrenome"})
})
public class Autor {


	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String sobrenome;

	private String nomeConhecido;

	private String sexo;

	private LocalDate dataNascimento;

	private LocalDate dataFalecimento;

	private String paisNascimento;

	private String estadoNascimento;

	private String cidadeNascimento;

	private String biografia;

	private String urlSiteOficial;

	private String urlFacebook;

	private String urlTwitter;
	
	private String urlWikipedia;
	
	@Builder.Default
	@OneToMany(mappedBy = "autor")
	private List<Livro> livros = new ArrayList<>();

}
