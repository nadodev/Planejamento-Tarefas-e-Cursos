# Planejador de Horário

Sistema para planejamento e gerenciamento de horários de estudo, desenvolvido com Spring Boot seguindo os princípios da Arquitetura Hexagonal (Ports and Adapters).

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA
- H2 Database (para desenvolvimento)
- Swagger/OpenAPI para documentação
- JUnit 5 e Mockito para testes

## Arquitetura

O projeto segue a Arquitetura Hexagonal (também conhecida como Ports and Adapters), com a seguinte estrutura:

```
src/main/java/br/com/leonardo/planejador_horario/
├── adapter/
│   ├── inbound/
│   │   ├── controller/    # Controllers REST
│   │   ├── dto/          # DTOs para request/response
│   │   └── exception/    # Handlers de exceção
│   └── outbound/
│       ├── entity/       # Entidades JPA
│       ├── mapper/       # Mapeadores entre domínio e entidades
│       └── persistence/  # Implementações de repositório
├── application/
│   └── port/
│       └── out/         # Interfaces de repositório
├── config/
│   └── security/       # Configurações de segurança e JWT
├── domain/
│   ├── exception/      # Exceções de domínio
│   ├── model/         # Modelos de domínio
│   └── validator/     # Validadores
└── usecase/           # Casos de uso da aplicação
    ├── auth/          # Casos de uso de autenticação
    └── impl/          # Implementações dos casos de uso
```

## Funcionalidades

### Módulo de Autenticação

- Registro de usuário
- Login com JWT
- Proteção de rotas com token Bearer

### Módulo de Usuários

- Criar usuário
- Listar usuários
- Buscar usuário por ID
- Atualizar usuário
- Deletar usuário

### Módulo de Cursos

- Criar curso
- Listar todos os cursos
- Listar cursos por usuário
- Deletar curso

## Como Executar

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/planejador_horario.git
cd planejador_horario
```

2. Execute a aplicação:
```bash
./mvnw spring-boot:run
```

3. Acesse a documentação da API:
```
http://localhost:9090/swagger-ui.html
```

## Testando a API

### Usando o Swagger

Acesse a documentação interativa em `http://localhost:9090/swagger-ui.html`. A interface do Swagger permite:

1. Visualizar todos os endpoints disponíveis
2. Testar as requisições diretamente pelo navegador
3. Ver os modelos de dados e exemplos de requisição/resposta
4. Autenticar-se usando o botão "Authorize" com o token JWT

## Exemplos de Requisições

### Registrar Usuário

```json
POST /api/usuarios
{
    "nome": "João da Silva",
    "email": "joao@email.com",
    "senha": "senha123"
}
```

### Login

```json
POST /api/auth/login
{
    "email": "joao@email.com",
    "senha": "senha123"
}
```

### Criar Curso (Autenticado)

```json
POST /api/cursos
{
    "nome": "Curso de Spring Boot",
    "descricao": "Curso completo de Spring Boot",
    "cargaHoraria": 40,
    "prioridade": 3,
    "prazoFinal": "2024-12-31"
}
```

## Segurança

O sistema utiliza autenticação JWT (JSON Web Token) com as seguintes características:

- Tokens com expiração de 24 horas
- Autenticação via header `Authorization: Bearer {token}`
- Endpoints públicos:
  - `/api/auth/login`
  - `/api/usuarios` (POST - registro)
  - `/v3/api-docs/**`
  - `/swagger-ui/**`
- Demais endpoints requerem autenticação

## Testes

O projeto inclui testes unitários e de integração. Para executar os testes:

```bash
./mvnw test
```

## Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Faça commit das suas alterações (`git commit -am 'Adiciona nova feature'`)
4. Faça push para a branch (`git push origin feature/nova-feature`)
5. Crie um Pull Request