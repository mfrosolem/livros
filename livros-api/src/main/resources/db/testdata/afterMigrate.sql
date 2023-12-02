set foreign_key_checks = 0;

lock tables genero write, editora write, autor write, livro write, foto_livro write,
grupo_permissao write, permissao write, usuario write, grupo write;

delete from foto_livro;
delete from livro;
delete from autor;
delete from genero;
delete from editora;
delete from grupo_permissao;
delete from permissao;
delete from usuario;
delete from grupo;


set foreign_key_checks = 1;


alter table autor auto_increment = 1;
alter table editora auto_increment = 1;
alter table genero auto_increment = 1;
alter table livro auto_increment = 1;
alter table permissao auto_increment = 1;
alter table usuario auto_increment = 1;
alter table grupo auto_increment = 1;


INSERT INTO genero (id, descricao) VALUES (1,'Romance');
INSERT INTO genero (id, descricao) VALUES (2,'Ficção');
INSERT INTO genero (id, descricao) VALUES (3,'Jornalismo Literário');
INSERT INTO genero (id, descricao) VALUES (4,'Autoajuda');
INSERT INTO genero (id, descricao) VALUES (5,'Autobiografia');
INSERT INTO genero (id, descricao) VALUES (6,'Biografia');
INSERT INTO genero (id, descricao) VALUES (7,'Comédia');
INSERT INTO genero (id, descricao) VALUES (8,'Contos');
INSERT INTO genero (id, descricao) VALUES (9,'Drama');
INSERT INTO genero (id, descricao) VALUES (10,'Economia');
INSERT INTO genero (id, descricao) VALUES (11,'História');
INSERT INTO genero (id, descricao) VALUES (12,'Suspense');
INSERT INTO genero (id, descricao) VALUES (13,'Teatro');
INSERT INTO genero (id, descricao) VALUES (14,'Terror');


INSERT INTO editora (id, nome, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (1,'Autêntica',NULL,NULL,NULL,NULL);
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (2,'Objetiva',NULL,NULL,NULL,NULL);
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (3,'Grupo Companhia das Letras','','','',''); 
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (4,'Grupo Penguin','','','',''); 
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (5,'Penguin Companhia',NULL,NULL,NULL,NULL); 
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (6,'Biblioteca Azul',NULL,NULL,NULL,NULL); 
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (7,'Grupo Record',NULL,NULL,NULL,NULL);


INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (1,'Joaquim ','Maria Machado de Assis','Machado de Assis','M','1839-06-21','1908-09-29','Brasil','Rio de Janeiro','Rio de Janeiro','Joaquim Maria Machado de Assis foi um escritor brasileiro, considerado por muitos críticos, estudiosos, escritores e leitores o maior nome da literatura brasileira.','','','','');
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (2,'François-Marie','Arouet','Voltaire','M','1694-11-21','1778-05-30','França',NULL,'Châtenay',NULL,NULL,NULL,NULL,NULL);
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (3,'Javier','Cercas Mena','Javier Cercas','M',NULL,NULL,'Espanha',NULL,'Cáceres',NULL,NULL,NULL,NULL,NULL);
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (4,'Zélia','Gattai Amado de Faria','Zélia Gattai','F','1916-07-02','2008-05-17','Brasil','São Paulo','São Paulo',NULL,NULL,NULL,NULL,NULL);
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (5,'Truman','Streckfus Persons','Truman Capote','M',NULL,NULL,'Estados Unidos',NULL,'Nova Orleans',NULL,NULL,NULL,NULL,NULL);
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (6,'Edgar Allan','Poe','Edgar Allan Poe','M','1809-01-19',NULL,'','','','Edgar Allan Poe foi um autor, poeta, editor e crítico literário estadunidense, integrante do movimento romântico em seu país.',NULL,NULL,NULL,NULL);
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (7,'Jorge','Amado','Jorge Amado','M',NULL,NULL,'','','','','','','','');
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, url_twitter, url_wikipedia) VALUES (8,'Gabriel José','García Márquez','Gabriel García Márquez','M','1927-03-06','2014-04-17','Colombia','','Aracataca','','','','','');


INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (1,'9788582850350','Dom Casmurro',NULL,'Português',NULL,NULL,NULL,3,2016,NULL,389,2,1,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (2,'9788563560933','O alienista',NULL,'Português',NULL,NULL,NULL,4,2015,NULL,88,2,1,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (5,'9788563560582','Cândido, ou o Otimismo',NULL,'Português','Clássicos',NULL,NULL,5,2016,NULL,128,2,2,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (6,'9788525053725','A Velocidade da Luz',NULL,'Português',NULL,NULL,'Sergio Molina',6,2013,1,245,1,3,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (7,'9788535913910','Anarquistas, graças a Deus',NULL,'Português',NULL,NULL,NULL,3,2009,1,323,1,4,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (8,'9788535904116','A Sangue Frio','','Português',NULL,NULL,NULL,3,2017,1,432,3,5,NULL);


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


INSERT INTO grupo (id, nome) values (1, 'Admin'), (2, 'Usuário'), (3, 'Visitante');


-- Adiciona todas as permissoes no grupo do admin
insert into grupo_permissao (grupo_id, permissao_id)
select 1, id from permissao;

-- Adiciona permissoes no grupo do usuario
insert into grupo_permissao (grupo_id, permissao_id)
select 2, id from permissao where nome like 'CONSULTAR_%' or nome like 'CADASTRAR_%';

-- Adiciona permissoes no grupo do visitante
insert into grupo_permissao (grupo_id, permissao_id)
select 3, id from permissao where nome like 'CONSULTAR_%';


INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (1, 1, 'Administrador', 'admin@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (2, 2, 'Maira', 'maira@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);


INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (3, 3, 'teste3', 'teste3@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (4, 3, 'teste4', 'teste4@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (5, 3, 'teste5', 'teste5@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (6, 3, 'teste6', 'teste6@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (7, 3, 'teste7', 'teste7@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (8, 3, 'teste8', 'teste8@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (9, 3, 'teste9', 'teste9@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (10, 3, 'teste10', 'teste10@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (11, 3, 'teste11', 'teste11@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (12, 3, 'teste12', 'teste12@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (13, 3, 'teste13', 'teste13@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (14, 3, 'teste14', 'teste14@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, grupo_id, nome, email, senha, data_cadastro) values (15, 3, 'teste15', 'teste15@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);




unlock tables;
