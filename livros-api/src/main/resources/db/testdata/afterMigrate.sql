set foreign_key_checks = 0;

lock tables genero write, editora write, autor write, livro write, foto_livro write, usuario_permissao write, 
permissao write, usuario write;

delete from foto_livro;
delete from livro;
delete from autor;
delete from genero;
delete from editora;
delete from usuario_permissao;
delete from permissao;
delete from usuario;


set foreign_key_checks = 1;


alter table autor auto_increment = 1;
alter table editora auto_increment = 1;
alter table genero auto_increment = 1;
alter table livro auto_increment = 1;
alter table permissao auto_increment = 1;
alter table usuario auto_increment = 1;


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


INSERT INTO usuario (id, nome, email, senha, data_cadastro) values (1, 'Administrador', 'admin@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);
INSERT INTO usuario (id, nome, email, senha, data_cadastro) values (2, 'Maira', 'maira@livros.com', '$2a$12$SM5Hz2FssKbAOrmYM4Yr..PAMyN8vXKA242ix/3PZnwefz5YopCFi', utc_timestamp);


INSERT INTO permissao (id, nome, descricao) values (1, 'ROLE_GENERO_PESQUISAR', 'Pode pesquisar genero');
INSERT INTO permissao (id, nome, descricao) values (2, 'ROLE_GENERO_CADASTRAR', 'Pode cadastrar e alterar genero');
INSERT INTO permissao (id, nome, descricao) values (3, 'ROLE_GENERO_REMOVER', 'Pode remover genero');
INSERT INTO permissao (id, nome, descricao) values (4, 'ROLE_EDITORA_PESQUISAR', 'Pode pesquisar editora');
INSERT INTO permissao (id, nome, descricao) values (5, 'ROLE_EDITORA_CADASTRAR', 'Pode cadastrar e alterar editora');
INSERT INTO permissao (id, nome, descricao) values (6, 'ROLE_EDITORA_REMOVER', 'Pode remover editora');
INSERT INTO permissao (id, nome, descricao) values (7, 'ROLE_AUTOR_PESQUISAR', 'Pode pesquisar autor');
INSERT INTO permissao (id, nome, descricao) values (8, 'ROLE_AUTOR_CADASTRAR', 'Pode cadastrar e alterar autor');
INSERT INTO permissao (id, nome, descricao) values (9, 'ROLE_AUTOR_REMOVER', 'Pode remover autor');
INSERT INTO permissao (id, nome, descricao) values (10, 'ROLE_LIVRO_PESQUISAR', 'Pode pesquisar livros');
INSERT INTO permissao (id, nome, descricao) values (11, 'ROLE_LIVRO_CADASTRAR', 'Pode cadastrar e alterar livro');
INSERT INTO permissao (id, nome, descricao) values (12, 'ROLE_LIVRO_REMOVER', 'Pode remover livro');


-- admin
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 1);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 2);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 3);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 4);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 5);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 6);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 7);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 8);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 9);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 10);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 11);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 12);

-- maira
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 1);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 4);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 7);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 10);



unlock tables;
