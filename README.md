# REST API com Spring Boot e Angular

![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)

## Descrição do Projeto

Projeto em desenvolvimento para organização de livros.

## :hamer: Funcionalidades do projeto

- Lista de Gêneros;
- Consulta de Gênero;
- Cadastro de Gênero;
- Edição de Gênero;
- Exclusão de Gênero;
- Lista de Editoras;
- Consulta de Editora;
- Cadastro de Editora;
- Edição de Editora;
- Exclusão de Editora;
- Lista de Autores;
- Consulta de Autor(a);
- Cadastro de Autor(a);
- Edição de Autor(a);
- Exclusão de Autor(a);
- Lista de Livros;
- Consulta de Livro;
- Cadastro de Livro;
- Edição de Livro;
- Exclusão de Livro;

## Tecnologias utilizadas
- Java 17
- Spring Boot 2.7.4
- MySQL 8.0
- JPA
- Maven
- Angular v14
- BootStrap 5.2.0

## Executando local

### Executando o back-end

Você precisa ter Java e Maven instalados e configurados local.

- Abra o projeto `livros-api` em sua IDE favorita como um projeto Maven; 
- Abra o arquivo `application.properties` e configure a aplicação;
- Execute o comando `mvn dependency:resolve` para fazer o download das dependências configuradas no arquivo `pom.xml`;
- Execute o projeto como Spring Boot application.

### Executando o front-end

Você precisa ter o Node.js / NPM instalados local.

1. Instala todas as dependências requeridas:

```
npm install
```

2. Execute o projeto:

```
ng serve
```


Abra o seu navegador e acesse **http://localhost:4200** (porta padrão Angular).