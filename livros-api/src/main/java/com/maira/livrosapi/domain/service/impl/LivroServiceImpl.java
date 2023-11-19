package com.maira.livrosapi.domain.service.impl;

import com.maira.livrosapi.domain.exception.EntidadeEmUsoException;
import com.maira.livrosapi.domain.exception.LivroNaoEncontradoException;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.repository.LivroRepository;
import com.maira.livrosapi.domain.service.AutorService;
import com.maira.livrosapi.domain.service.EditoraService;
import com.maira.livrosapi.domain.service.GeneroService;
import com.maira.livrosapi.domain.service.LivroService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivroServiceImpl implements LivroService {

	private static final String MSG_LIVRO_EM_USO = "Livro de código %d não pode ser removido, pois está em uso";
	private static final String MSG_LIVRO_UNIQUE = "Livro de título %s já cadastrado";

	private final LivroRepository livroRepository;
	private final AutorService cadastroAutor;
	private final GeneroService cadastroGenero;
	private final EditoraService cadastroEditora;

	public LivroServiceImpl(LivroRepository livroRepository, AutorService cadastroAutor, GeneroService cadastroGenero,
							EditoraService cadastroEditora) {
		this.livroRepository = livroRepository;
		this.cadastroAutor = cadastroAutor;
		this.cadastroGenero = cadastroGenero;
		this.cadastroEditora = cadastroEditora;
	}

	@Override
	public Livro buscarOuFalhar(Long id) {
		return livroRepository.findById(id).orElseThrow(() -> new LivroNaoEncontradoException(id));
	}

	@Transactional
	@Override
	public Livro salvar(Livro entidade) {
		try {
			Long autorId = entidade.getAutor().getId();
			Autor autor = cadastroAutor.buscarOuFalhar(autorId);
			entidade.setAutor(autor);

			Long generoId = entidade.getGenero().getId();
			Genero genero = cadastroGenero.buscarOuFalhar(generoId);
			entidade.setGenero(genero);

			Long editoraId = entidade.getEditora().getId();
			Editora editora = cadastroEditora.buscarOuFalhar(editoraId);
			entidade.setEditora(editora);

			return livroRepository.save(entidade);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_LIVRO_UNIQUE, entidade.getTitulo()));
		}
	}

	@Transactional
	@Override
	public void excluir(Long id) {
		try {
			livroRepository.deleteById(id);
			livroRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new LivroNaoEncontradoException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_LIVRO_EM_USO, id));
		}
	}

	@Override
	public Page<Livro> listByContaining(String contain, Pageable pageable) {
		return livroRepository.findByTituloContaining(contain, pageable);
	}

}
