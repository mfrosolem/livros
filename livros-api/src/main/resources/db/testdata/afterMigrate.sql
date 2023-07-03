set foreign_key_checks = 0;

lock tables genero write, editora write, autor write, livro write, foto_livro write;

delete from foto_livro;
delete from livro;
delete from autor;
delete from genero;
delete from editora;

set foreign_key_checks = 1;

alter table autor auto_increment = 1;
alter table editora auto_increment = 1;
alter table genero auto_increment = 1;
alter table livro auto_increment = 1;

INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (6,'Edgar Allan','Poe','Edgar Allan Poe','M','1809-01-19 00:00:00',NULL,'','','','Edgar Allan Poe foi um autor, poeta, editor e crítico literário estadunidense, integrante do movimento romântico em seu país.',NULL,NULL,NULL,NULL);
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (7,'Jorge','Amado','Jorge Amado','M',NULL,NULL,'','','','','','','','');
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (8,'Gabriel José','García Márquez','Gabriel García Márquez','M','1927-03-06 00:00:00','2014-04-17 00:00:00','Colombia','','Aracataca','','','','','');
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (9,'Joaquim ','Maria Machado de Assis','Machado de Assis','M','1839-06-21 00:00:00','1908-09-29 00:00:00','Brasil','Rio de Janeiro','Rio de Janeiro','Joaquim Maria Machado de Assis foi um escritor brasileiro, considerado por muitos críticos, estudiosos, escritores e leitores o maior nome da literatura brasileira.','','','','');
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (10,'François-Marie','Arouet','Voltaire','M','1694-11-21 00:00:00','1778-05-30 00:00:00','França',NULL,'Châtenay',NULL,NULL,NULL,NULL,NULL);
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (11,'Javier','Cercas Mena','Javier Cercas','M',NULL,NULL,'Espanha',NULL,'Cáceres',NULL,NULL,NULL,NULL,NULL);
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (12,'Zélia','Gattai Amado de Faria','Zélia Gattai','F','1916-07-02 00:00:00','2008-05-17 00:00:00','Brasil','São Paulo','São Paulo',NULL,NULL,NULL,NULL,NULL);
INSERT INTO autor (id, nome, sobrenome, nome_conhecido, sexo, data_nascimento, data_falecimento, pais_nascimento, estado_nascimento, cidade_nascimento, biografia, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (13,'Truman','Streckfus Persons','Truman Capote','M',NULL,NULL,'Estados Unidos',NULL,'Nova Orleans',NULL,NULL,NULL,NULL,NULL);


INSERT INTO editora (id, nome, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (1,'Autêntica',NULL,NULL,NULL,NULL);
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (2,'Objetiva',NULL,NULL,NULL,NULL);
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (7,'Grupo Record',NULL,NULL,NULL,NULL);
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (8,'Grupo Companhia das Letras','','','','');
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (9,'Grupo Penguin','','','','');
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (10,'Penguin Companhia',NULL,NULL,NULL,NULL);
INSERT INTO editora (id, nome, url_site_oficial, url_facebook, ult_twitter, url_wikipedia) VALUES (11,'Biblioteca Azul',NULL,NULL,NULL,NULL);

INSERT INTO genero (id, descricao) VALUES (15,'Autoajuda');
INSERT INTO genero (id, descricao) VALUES (14,'Autobiografia');
INSERT INTO genero (id, descricao) VALUES (13,'Biografia');
INSERT INTO genero (id, descricao) VALUES (9,'Comédia');
INSERT INTO genero (id, descricao) VALUES (46,'Contos');
INSERT INTO genero (id, descricao) VALUES (7,'Drama');
INSERT INTO genero (id, descricao) VALUES (43,'Economia');
INSERT INTO genero (id, descricao) VALUES (5,'Ficção');
INSERT INTO genero (id, descricao) VALUES (10,'História');
INSERT INTO genero (id, descricao) VALUES (47,'Jornalismo Literário');
INSERT INTO genero (id, descricao) VALUES (1,'Romance');
INSERT INTO genero (id, descricao) VALUES (8,'Suspense');
INSERT INTO genero (id, descricao) VALUES (45,'Teatro');
INSERT INTO genero (id, descricao) VALUES (6,'Terror');

INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (1,'9788582850350','Dom Casmurro',NULL,'Português',NULL,NULL,NULL,8,2016,NULL,389,5,9,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (2,'9788563560933','O alienista',NULL,'Português',NULL,NULL,NULL,9,2015,NULL,88,5,9,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (5,'9788563560582','Cândido, ou o Otimismo',NULL,'Português','Clássicos',NULL,NULL,10,2016,NULL,128,5,10,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (6,'9788525053725','A Velocidade da Luz',NULL,'Português',NULL,NULL,'Sergio Molina',11,2013,1,245,1,11,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (7,'9788535913910','Anarquistas, graças a Deus',NULL,'Português',NULL,NULL,NULL,8,2009,1,323,1,12,NULL);
INSERT INTO livro (id, isbn, titulo, subtitulo, idioma, serie_colecao, volume, tradutor, editora_id, ano, edicao, paginas, genero_id, autor_id, sinopse) VALUES (8,'9788535904116','A Sangue Frio','','Português',NULL,NULL,NULL,8,2017,1,432,47,13,NULL);

unlock tables;