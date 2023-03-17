# REST API com Spring Boot e Angular

![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)

## Descrição do Projeto

Projeto em desenvolvimento para organização de livros.

## :hammer: Funcionalidades do projeto

- `Gênero`: Cadastro, edição, exclusão, consulta e lista de gêneros;
- `Editora`: Cadastro, edição, exclusão, consulta e lista de editoras;
- `Autor(a)`: Cadastro, edição, exclusão, consulta e lista de autores;
- `Livro`: Cadastro, edição, exclusão, consulta e lista de livros;
- `Foto do livro`: Upload, substituição e remoção da foto de capa do livro;

## :computer: Tecnologias utilizadas
- Java 17
- Spring Boot 3.0.0 (Spring 6)
- MySQL 8.0
- JPA
- Maven
- Angular v14
- BootStrap 5.2.3

## IDEs utilizadas
 - Spring Tool Suite 4.15.1
 - Visual Studio Code 1.76.1

## Executando local

### Executando o back-end

Você precisa ter Java e Maven instalados e configurados local.

1. Abra o projeto `livros-api` em sua IDE favorita como um projeto Maven; 
2. Abra o arquivo `application.properties` e configure a aplicação;
3. Execute o comando abaixo para fazer o download das dependências configuradas no arquivo `pom.xml`:

```
mvn dependency:resolve
```

4. Execute o projeto como Spring Boot application.

### Executando o front-end

Você precisa ter o Node.js / NPM e Angular instalados local.

1. Abra o projeto `livros-ui-bootstrap` na IDE Visual Studio Code;
2. Execute o comando abaixo para instalar todas as dependências requeridas:

```
npm install
```

3. Execute o projeto:

```
ng serve
```

4. Abra o seu navegador e acesse **http://localhost:4200** (porta padrão Angular).