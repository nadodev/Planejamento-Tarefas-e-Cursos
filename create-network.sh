#!/bin/bash

echo "Verificando rede Docker existente..."

# Remove a rede se já existir
if docker network ls | grep -q planejador-network; then
    echo "Removendo rede antiga..."
    docker network rm planejador-network || true
fi

# Cria a nova rede
echo "Criando nova rede Docker..."
docker network create --driver bridge planejador-network

# Verifica se a rede foi criada com sucesso
if docker network ls | grep -q planejador-network; then
    echo "Rede planejador-network criada com sucesso"
    docker network inspect planejador-network
else
    echo "Erro ao criar a rede planejador-network"
    exit 1
fi 