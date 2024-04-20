docker pull postgres:latest
docker run --name mappin-cliente-db -p 5432:5432 -e POSTGRES_USER=mappin -e POSTGRES_PASSWORD=mappinCliente -e POSTGRES_DB=mappin-cliente-db -d postgres