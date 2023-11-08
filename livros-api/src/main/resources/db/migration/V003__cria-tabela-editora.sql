create table editora (
	id bigint not null auto_increment,
	nome varchar(60) not null,
	url_site_oficial varchar(100),
	url_facebook varchar(100),
	url_twitter varchar(100),
	url_wikipedia varchar(100),
	primary key (id)
) engine=InnoDB default charset=utf8;

alter table editora add constraint uq_editora_nome unique (nome);