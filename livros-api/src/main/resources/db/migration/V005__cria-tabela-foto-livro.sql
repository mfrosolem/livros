create table foto_livro (
  livro_id bigint not null,
  nome_arquivo varchar(150) not null,
  descricao varchar(150),
  content_type varchar(80) not null,
  tamanho int not null,

  primary key (livro_id),
  constraint fk_foto_livro_livro foreign key (livro_id) references livro (id)
) engine=InnoDB default charset=utf8;