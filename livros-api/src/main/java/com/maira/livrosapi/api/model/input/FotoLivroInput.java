package com.maira.livrosapi.api.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.maira.livrosapi.core.validation.FileContentType;
import com.maira.livrosapi.core.validation.FileSize;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FotoLivroInput {

	@Schema(description = "Arquivo da foto do produto (máximo 500KB, apenas JPG e PNG)")
	@NotNull
	@FileSize(max = "500KB")
	@FileContentType(allowed = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	private MultipartFile arquivo;

	@Schema(description = "Descrição da foto da capa")
	@NotBlank
	private String descricao;

}
