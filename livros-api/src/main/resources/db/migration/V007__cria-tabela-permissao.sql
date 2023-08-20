CREATE TABLE permissao (
	id bigint not null auto_increment,
	nome VARCHAR(100) NOT NULL,
	descricao VARCHAR(100) NOT NULL,
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
