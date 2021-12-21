package org.generation.blogPessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogPessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll
	void start() {

		usuarioRepository.save(new Usuario(0L, "João da Silva", "joao@email.com", "134652"));
		usuarioRepository.save(new Usuario(0L, "Manuela da Silva", "manuela@email.com", "134652"));
		usuarioRepository.save(new Usuario(0L, "Adriana da Silva", "adriana@email.com", "134652"));
		usuarioRepository.save(new Usuario(0L, "Paulo da Silva", "paulo@email.com", "134652"));
	}

	@Test
	@DisplayName("Retorna 1 usuário")
	public void deveRetornarUmUsuário() {

		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@email.com");
		assertTrue(usuario.get().getUsuario().equals("joao@email.com"));
	}

	@Test
	@DisplayName("Pesquisa email inválido")
	void searchEmailInvalidReturnOptionalEmpty() {

		Optional<Usuario> optional = usuarioRepository.findByUsuario("");
		assertTrue(optional.isEmpty());
	}

	@Test
	@DisplayName("Pesquisa nome Silva")
	void searchFromSilvaReturnFourUsers() {

		List<Usuario> list = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
		assertEquals(4, list.size());
	}

	@Test
	@DisplayName("Pesquisa nome Manuela")
	void searchFromManuelaReturnOneUser() {

		List<Usuario> list = usuarioRepository.findAllByNomeContainingIgnoreCase("Manuela");
		assertEquals(1, list.size());
	}
}