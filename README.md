# REST API com Spring Boot e Angular

## Tecnologias
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