CREATE TABLE usuario_permissao (
	usuario_id bigint not null,
	permissao_id BIGINT(20) NOT NULL,
	PRIMARY KEY (usuario_id, permissao_id),
	FOREIGN KEY (usuario_id) REFERENCES usuario(id),
	FOREIGN KEY (permissao_id) REFERENCES permissao(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table usuario_permissao add constraint fk_usuario_permissao_usuario
foreign key (usuario_id) references usuario (id);

alter table usuario_permissao add constraint fk_usuario_permissao_permissao
foreign key (permissao_id) references permissao (id);
