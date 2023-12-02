create table grupo (
	id bigint not null auto_increment,
	nome varchar(60) not null,

	primary key (id)
) engine=InnoDB default charset=utf8;

alter table grupo add constraint uq_grupo_nome unique (nome);
