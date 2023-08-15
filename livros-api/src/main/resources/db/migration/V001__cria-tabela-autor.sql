create table autor (
	id bigint not null auto_increment,
	nome varchar(30) not null,
	sobrenome varchar(40) not null,
	nome_conhecido varchar(60),
	sexo char(1),
	data_nascimento date,
	data_falecimento date,
	pais_nascimento varchar(40),
	estado_nascimento varchar(40),
	cidade_nascimento varchar(40),
	biografia text,
	url_site_oficial varchar(100),
	url_facebook varchar(100),
	url_twitter varchar(100),
	url_wikipedia varchar(100),
	primary key (id)
) engine=InnoDB default charset=utf8;

alter table autor add constraint uc_autor_nome_sobrenome unique (nome, sobrenome);