docker compose down


# build backend image
docker build -t livros-api:latest ./livros-api

# build frontend image
docker build -t livros-ui:latest ./livros-ui-bootstrap

#start environment
docker compose up -d --build --force-recreate --remove-orphans