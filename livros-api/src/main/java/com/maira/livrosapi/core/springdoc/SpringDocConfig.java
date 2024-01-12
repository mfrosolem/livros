package com.maira.livrosapi.core.springdoc;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.maira.livrosapi.api.exception.Problem;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2, 
flows = @OAuthFlows(authorizationCode = @OAuthFlow(authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}", tokenUrl = "${springdoc.oAuthFlow.tokenUrl}", 
scopes = {
		@OAuthScope(name = "READ", description = "read scope"),
		@OAuthScope(name = "WRITE", description = "write scope"),
		@OAuthScope(name = "DELETE", description = "delete scope") })))
public class SpringDocConfig {
	
	private static final String BAD_REQUEST_RESPONSE = "BadRequestResponse";
	private static final String NOT_FOUND_RESPONSE = "NotFoundResponse";
	private static final String NOT_ACCEPTABLE_RESPONSE = "NotAcceptableResponse";
    private static final String INTERNAL_SERVER_ERROR_RESPONSE = "InternalServerErrorResponse";
    private static final String UNSUPPORTED_MEDIA_TYPE = "UnsupportedMediaType";
	
    @Bean
    public OpenAPI openAPI() {
    	return new OpenAPI()
    			.info(new Info()
    					.title("Livros API")
    					.description("REST API para livros")
    					.license(new License()
    							.name("Apache 2.0")
    							.url("http://springdoc.com")
    							)
    					)
    			.externalDocs(new ExternalDocumentation()
    					.description("Livros")
    					.url("https://livros.com")
    					)
    			.tags(Arrays.asList(
    					new Tag().name("Generos").description("Gerencia os gêneros"),
    					new Tag().name("Autores").description("Gerencia os autores"),
    					new Tag().name("Editoras").description("Gerencia as editoras"),
    					new Tag().name("Livros").description("Gerencia os livros"),
    					new Tag().name("Fotos").description("Gerencia as fotos de capa dos livros")
    					))
    			.components(new Components()
    					.schemas(gerarSchemas())
    					.responses(gerarResponses())
    					);
    }
    
    @Bean
    public OpenApiCustomizer openApiCustomizer() {
    	return openApi -> {
    		openApi.getPaths()
    			.values()
    			.forEach(pathItem -> pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
    				ApiResponses responses = operation.getResponses();
    				switch (httpMethod) {
					case GET: 
						responses.addApiResponse(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
						responses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
						break;
					case POST: 
						responses.addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
						responses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
						responses.addApiResponse(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
						responses.addApiResponse(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()), new ApiResponse().$ref(UNSUPPORTED_MEDIA_TYPE));
						break;
					case PUT: 
						responses.addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
						responses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
						responses.addApiResponse(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
						responses.addApiResponse(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()), new ApiResponse().$ref(UNSUPPORTED_MEDIA_TYPE));
						break;
					case DELETE: 
						responses.addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
						responses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
						break;
					default:
						responses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
						break;
					}
    				
    			}) 
    		);
    	};
    }
	
	
	private Map<String, Schema> gerarSchemas() {
		final Map<String, Schema> schemaMap = new HashMap<>();
		
		Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
		Map<String, Schema> problemObjectSchema = ModelConverters.getInstance().read(Problem.Object.class);
		
		schemaMap.putAll(problemSchema);
		schemaMap.putAll(problemObjectSchema);
		
		return schemaMap;
	}
	
	
	private Map<String, ApiResponse> gerarResponses() {
		
		final Map<String, ApiResponse> apiResponseMap = new HashMap<>();
		
		Content content = new Content()
				.addMediaType(APPLICATION_JSON_VALUE, 
						new MediaType().schema(new Schema<Problem>().$ref("Problema")));
		
		apiResponseMap.put(BAD_REQUEST_RESPONSE, new ApiResponse()
				.description("Requisição inválida")
				.content(content));
		
		apiResponseMap.put(NOT_FOUND_RESPONSE, new ApiResponse()
				.description("Recurso não encontrado")
				.content(content));
		
		apiResponseMap.put(NOT_ACCEPTABLE_RESPONSE, new ApiResponse()
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.content(content));
		
		apiResponseMap.put(INTERNAL_SERVER_ERROR_RESPONSE, new ApiResponse()
				.description("Erro interno no servidor")
				.content(content));
		
		apiResponseMap.put(UNSUPPORTED_MEDIA_TYPE, new ApiResponse()
				.description("Requisição recusada porque o corpo está em um formato não suportado")
				.content(content));
		
		
		
		return apiResponseMap;
		
	}
	
	

}
