CREATE TABLE usuario (
	id bigint not null auto_increment,
	nome VARCHAR(80) NOT NULL,
	email VARCHAR(255) NOT NULL,
	senha VARCHAR(255) NOT NULL,
	data_cadastro datetime not null,
	
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
