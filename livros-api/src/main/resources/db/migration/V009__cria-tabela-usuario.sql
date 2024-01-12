CREATE TABLE usuario (
	id bigint not null auto_increment,
	grupo_id bigint not null,
	nome VARCHAR(80) NOT NULL,
	email VARCHAR(255) NOT NULL,
	senha VARCHAR(255) NOT NULL,
	data_cadastro datetime not null,
	primeiro_acesso tinyint(1) not null,
	
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table usuario add constraint fk_usuario_grupo
foreign key (grupo_id) references grupo (id);

alter table usuario add constraint uq_usuario_email unique (email);
