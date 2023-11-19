INSERT INTO permissao (id, nome, descricao) values (1, 'CONSULTAR_GENERO', 'Pode pesquisar genero');
INSERT INTO permissao (id, nome, descricao) values (2, 'CADASTRAR_GENERO', 'Pode cadastrar e editar genero');
INSERT INTO permissao (id, nome, descricao) values (3, 'REMOVER_GENERO', 'Pode remover genero');
INSERT INTO permissao (id, nome, descricao) values (4, 'CONSULTAR_EDITORA', 'Pode pesquisar editora');
INSERT INTO permissao (id, nome, descricao) values (5, 'CADASTRAR_EDITORA', 'Pode cadastrar e editar editora');
INSERT INTO permissao (id, nome, descricao) values (6, 'REMOVER_EDITORA', 'Pode remover editora');
INSERT INTO permissao (id, nome, descricao) values (7, 'CONSULTAR_AUTOR', 'Pode pesquisar autor');
INSERT INTO permissao (id, nome, descricao) values (8, 'CADASTRAR_AUTOR', 'Pode cadastrar e editar autor');
INSERT INTO permissao (id, nome, descricao) values (9, 'REMOVER_AUTOR', 'Pode remover autor');
INSERT INTO permissao (id, nome, descricao) values (10, 'CONSULTAR_LIVRO', 'Pode pesquisar livros');
INSERT INTO permissao (id, nome, descricao) values (11, 'CADASTRAR_LIVRO', 'Pode cadastrar e editar livro');
INSERT INTO permissao (id, nome, descricao) values (12, 'REMOVER_LIVRO', 'Pode remover livro');
INSERT INTO permissao (id, nome, descricao) values (13, 'CONSULTAR_USUARIOS_GRUPOS_PERMISSOES', 'Permite consultar usuários, grupos e permissões');
INSERT INTO permissao (id, nome, descricao) values (14, 'CADASTRAR_USUARIOS_GRUPOS_PERMISSOES', 'Permite criar ou editar usuários, grupos e permissões');


INSERT INTO grupo (id, nome) values (1, 'Admin');


-- Adiciona todas as permissoes no grupo do admin
insert into grupo_permissao (grupo_id, permissao_id)
select 1, id from permissao;



INSERT INTO usuario (id, nome, email, senha, data_cadastro) values (1, 'Teste', 'teste@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);

-- admin
INSERT INTO usuario_grupo (usuario_id, grupo_id) values (1, 1);
