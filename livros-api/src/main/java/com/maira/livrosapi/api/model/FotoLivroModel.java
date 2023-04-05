package com.maira.livrosapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoLivroModel {

	@Schema(example = "759def91-6d60-4d84-ba57-3faad8930330_DOM_CASMURRO_1468012492180SK1468012492B.jpg")
	private String nomeArquivo;
	
	@Schema(example = "Capa")
	private String descricao;
	
	@Schema(example = "image/jpeg")
	private String contentType;
	
	@Schema(example = "6778")
	private Long tamanho;

}
