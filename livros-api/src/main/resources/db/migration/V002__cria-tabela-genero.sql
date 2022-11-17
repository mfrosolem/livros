create table genero (
	id bigint not null auto_increment,
	descricao varchar(60) not null,
	primary key (id)
) engine=InnoDB default charset=utf8;

alter table genero add constraint uc_genero_descricao unique (descricao);