package com.maira.livrosapi.domain.service;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.LivroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.repository.LivroRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivroService {

	private static final String MSG_LIVRO_EM_USO = "Livro de código %d não pode ser removido, pois está em uso";
	private static final String MSG_LIVRO_UNIQUE = "Livro de título %s já cadastrado";

	private final LivroRepository livroRepository;
	private final AutorService cadastroAutor;
	private final GeneroService cadastroGenero;
	private final EditoraService cadastroEditora;

	public LivroService(LivroRepository livroRepository, AutorService cadastroAutor, GeneroService cadastroGenero,
						EditoraService cadastroEditora) {
		this.livroRepository = livroRepository;
		this.cadastroAutor = cadastroAutor;
		this.cadastroGenero = cadastroGenero;
		this.cadastroEditora = cadastroEditora;
	}

	public Livro buscarOuFalhar(Long livroId) {
		return livroRepository.findById(livroId).orElseThrow(() -> new LivroNaoEncontradoException(livroId));
	}

	@Transactional
	public Livro salvar(Livro livro) {
		try {
			Long autorId = livro.getAutor().getId();
			Autor autor = cadastroAutor.buscarOuFalhar(autorId);
			livro.setAutor(autor);

			Long generoId = livro.getGenero().getId();
			Genero genero = cadastroGenero.buscarOuFalhar(generoId);
			livro.setGenero(genero);

			Long editoraId = livro.getEditora().getId();
			Editora editora = cadastroEditora.buscarOuFalhar(editoraId);
			livro.setEditora(editora);

			return livroRepository.save(livro);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_LIVRO_UNIQUE, livro.getTitulo()));
		}
	}

	@Transactional
	public void excluir(Long livroId) {
		try {
			livroRepository.deleteById(livroId);
			livroRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new LivroNaoEncontradoException(livroId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_LIVRO_EM_USO, livroId));
		}
	}

	public Page<Livro> listByTituloContaining(String titulo, Pageable pageable) {
		return livroRepository.findByTituloContaining(titulo, pageable);
	}

}
