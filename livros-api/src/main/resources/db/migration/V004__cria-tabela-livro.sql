create table livro (
	id bigint not null auto_increment,
	isbn varchar(13) not null,
	titulo varchar(50) not null,
	subtitulo varchar(50),
	idioma varchar(15) not null,
	serie_colecao varchar(20),
	volume bigint,
	tradutor varchar(60),
	editora_id bigint not null,
	ano bigint,
	edicao bigint,
	paginas bigint not null,
	genero_id bigint not null,
	autor_id bigint not null,
	sinopse varchar(500),
	
	primary key (id)
) engine=InnoDB default charset=utf8;

alter table livro add constraint fk_livro_autor
foreign key (autor_id) references autor (id);

alter table livro add constraint fk_livro_genero
foreign key (genero_id) references genero (id);

alter table livro add constraint fk_livro_editora
foreign key (editora_id) references editora (id);

alter table livro add constraint uc_livro_isbn unique (isbn);