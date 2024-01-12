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
- `Grupo`: Cadastro, edição, exclusão, consulta, lista e vinculação com permissões;
- `Permissão`: Consulta e lista de permissões;
- `Usuário`: Cadastro, edição, exclusão, consulta e lista de usuários;
- `Alteração de Senha`: Validação e alteração da senha do usuário logado;
- `Envio de E-mail`: Envio de e-mail com a senha gerada após cadastro do usuário;
- `Login`;
- Testes Unitários

## :computer: Tecnologias utilizadas
- Java 17
- Spring Boot 3.1.5 (Spring 6)
- MySQL 8.0
- JPA
- Maven
- Angular v16
- BootStrap 5.3.2
- Docker

## IDEs utilizadas
 - IntelliJ IDEA
 - Visual Studio Code

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

5. Para conhecer a documentação feita com SpringDoc, abra o navegador e acesse **http://local-livros.com:8080/swagger-ui/index.html**

### Executando o front-end

Você precisa ter o Node.js / NPM e Angular instalados local.

1. Abra o projeto `livros-ui-bootstrap` na IDE Visual Studio Code;
2. Execute o comando abaixo para instalar todas as dependências requeridas:

```
npm install
```

3. Execute o projeto:

```
ng serve --port 8000
```

4. Abra o seu navegador e acesse **http://localhost:8000**

### Executando o back-end e front-end com Docker

Você precisa ter o Docker instalado.

1. Entre no diretório do projeto 
2. Execute o comando abaixo para construir as imagens e subir os containers:

```
./start.sh
```
3. Abra o seu navegador e acesse **http://localhost:8000**
4. Para parar as aplicações derrubar e remover os containers:

```
./stop.sh
```


