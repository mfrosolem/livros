package com.maira.livrosapi;

//import static io.restassured.RestAssured.given;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//
//import com.maira.livrosapi.util.ResourceTestUtils;
//
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;

//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CadastroEditoraIT {
	
//	@LocalServerPort
//	private int port;
//	
//	private String jsonCorretoEditoraAutentica;
//	
//	@BeforeEach
//	public void setUp() {
//		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//		RestAssured.port = this.port;
//		RestAssured.basePath = "/editoras";
//		
//		jsonCorretoEditoraAutentica = ResourceTestUtils.getContentResourceFrom("/json/correto/editora-autentica.json");
//	}
//	
//	@Test
//	public void deveRetornarStatus200_QuandoConsultarEditoras() {
//		given()
//			.contentType(ContentType.JSON)
//		.when()
//			.get()
//		.then()
//			.statusCode(HttpStatus.OK.value());
//	}
//	
//	//@Test
//	public void deveRetornarStatus201_QuandoCadastrarEditora() {
//		given()
//			.contentType(ContentType.JSON)
//			.accept(ContentType.JSON)
//			.body(jsonCorretoEditoraAutentica)
//		.when()
//			.post()
//		.then()
//			.statusCode(HttpStatus.CREATED.value());
//	}
		

}
