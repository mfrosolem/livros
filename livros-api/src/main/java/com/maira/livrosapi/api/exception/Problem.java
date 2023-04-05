package com.maira.livrosapi.api.exception;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(value = Include.NON_NULL)
@Getter
@Builder
@Schema(name = "Problema")
public class Problem {

	@Schema(example = "400")
	private Integer status;
	
	@Schema(example = "https://livros.com.br/dados-invalidos")
	private String type;
	
	@Schema(example = "Dados inválidos")
	private String title;
	
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private String detail;
	
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private String userMessage;
	
	@Schema(example = "2022-07-15T11:21:50.902245498Z")
	private OffsetDateTime timestamp;
	
	@Schema(description = "Lista de objetos ou campos que geraram o erro")
	private List<Object> objects;

	@Getter
	@Builder
	@Schema(name = "ObjetoProblema")
	public static class Object {
		
		@Schema(example = "titulo")
		private String name;
		
		@Schema(example = "O titulo é obrigatório")
		private String userMessage;
	}

}