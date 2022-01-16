package org.generation.blogPessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogPessoal.model.Usuario;
import org.generation.blogPessoal.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Test
	@Order(1)
	@DisplayName("Cadastra Um Usuário")
	public void deveCriarUmUsuario() {

		HttpEntity<Usuario> request = new HttpEntity<Usuario>(
				new Usuario(0L, "Jessica da Silva", "jessica@email.com", "134652"));

		ResponseEntity<Usuario> response = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST,
				request, Usuario.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(request.getBody().getNome(), response.getBody().getNome());
		assertEquals(request.getBody().getUsuario(), response.getBody().getUsuario());
	}

	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação de Usuáio")
	public void naoDeveDuplicarUsuario() {

		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Maria da Silva", "maria@email.com", "134652"));

		HttpEntity<Usuario> request = new HttpEntity<Usuario>(new Usuario(0L,
				"Maria da Silva", "maria@email.com", "134652"));

		ResponseEntity<Usuario> response = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST,request, Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	@Order(3)
	@DisplayName("Alterar um Usuário")
	public void deveAtualizarUmUsuario() {

		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123"));

		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), 
			"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate
			.withBasicAuth("boaz", "boaz")
			.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
	}

	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
			
		usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Marques", "sabrina@email.com", "sabrina123"));

		usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo Sanches", "ricardo@email.com", "ricardo123"));
			
		ResponseEntity<String> response = testRestTemplate
			.withBasicAuth("boaz", "boaz")
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
}
