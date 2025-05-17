#!/bin/bash

# Verifica se a rede já existe
if ! docker network ls | grep -q planejador-network; then
    # Cria a rede se não existir
    docker network create planejador-network
fi 