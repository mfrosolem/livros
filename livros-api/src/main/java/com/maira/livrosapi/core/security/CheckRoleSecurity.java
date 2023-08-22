package com.maira.livrosapi.core.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

public @interface CheckRoleSecurity {

	public @interface Generos {

		@PreAuthorize("@livrosSecurity.podeConsultarGeneros()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {
		}

		@PreAuthorize("@livrosSecurity.podeCadastrarEditarGenero()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeCadastrarEditar {
		}

		@PreAuthorize("@livrosSecurity.podeRemoverGenero()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeRemover {
		}
	}

	public @interface Editoras {

		@PreAuthorize("@livrosSecurity.podeConsultarEditoras()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {
		}

		@PreAuthorize("@livrosSecurity.podeCadastrarEditarEditora()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeCadastrarEditar {
		}

		@PreAuthorize("@livrosSecurity.podeRemoverEditora()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeRemover {
		}
	}
	
	public @interface Autores {

		@PreAuthorize("@livrosSecurity.podeConsultarAutores()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {
		}

		@PreAuthorize("@livrosSecurity.podeCadastrarEditarAutor()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeCadastrarEditar {
		}

		@PreAuthorize("@livrosSecurity.podeRemoverAutor()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeRemover {
		}
	}
	
	public @interface Livros {

		@PreAuthorize("@livrosSecurity.podeConsultarLivros()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeConsultar {
		}

		@PreAuthorize("@livrosSecurity.podeCadastrarEditarLivro()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeCadastrarEditar {
		}

		@PreAuthorize("@livrosSecurity.podeRemoverLivro()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface PodeRemover {
		}
	}

}
