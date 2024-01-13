# LivrosApi

## Executando local

### Executando o back-end

Você precisa ter Java, Maven e MySQL instalados e configurados local.

1. Abra o projeto `livros-api` em sua IDE favorita como um projeto Maven; 
2. Abra o arquivo `application.properties` e configure a aplicação;
3. Execute o comando abaixo para fazer o download das dependências configuradas no arquivo `pom.xml`:

```
mvn dependency:resolve
```

4. Execute o projeto como Spring Boot application.

5. Para conhecer a documentação feita com SpringDoc, abra o navegador e acesse **http://localhost:8080/swagger-ui/index.html**

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
