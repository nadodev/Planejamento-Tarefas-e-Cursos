# Exemplos de Uso da API

## Autenticação

### 1. Registro de Usuário

```http
POST /api/usuarios
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123"
}
```

Resposta:
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com"
}
```

### 2. Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "senha": "senha123"
}
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "expiraEm": 86400000
}
```

## Tarefas

### 1. Criar Tarefa

```http
POST /api/tarefas
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Implementar API REST",
  "descricao": "Desenvolver endpoints para gerenciamento de tarefas",
  "status": "PENDENTE",
  "prioridade": "ALTA",
  "categoria": "DESENVOLVIMENTO",
  "dataInicio": "2024-03-20",
  "dataFim": "2024-03-25"
}
```

Resposta:
```json
{
  "id": 1,
  "titulo": "Implementar API REST",
  "descricao": "Desenvolver endpoints para gerenciamento de tarefas",
  "status": "PENDENTE",
  "prioridade": "ALTA",
  "categoria": "DESENVOLVIMENTO",
  "dataInicio": "2024-03-20",
  "dataFim": "2024-03-25",
  "usuario": {
    "id": 1,
    "nome": "João Silva"
  }
}
```

### 2. Listar Tarefas (Paginado)

```http
GET /api/tarefas?page=0&size=10
Authorization: Bearer {token}
```

Resposta:
```json
{
  "content": [
    {
      "id": 1,
      "titulo": "Implementar API REST",
      "status": "PENDENTE",
      "prioridade": "ALTA",
      "categoria": "DESENVOLVIMENTO",
      "dataInicio": "2024-03-20",
      "dataFim": "2024-03-25"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true
}
```

### 3. Buscar Tarefa por ID

```http
GET /api/tarefas/1
Authorization: Bearer {token}
```

Resposta:
```json
{
  "id": 1,
  "titulo": "Implementar API REST",
  "descricao": "Desenvolver endpoints para gerenciamento de tarefas",
  "status": "PENDENTE",
  "prioridade": "ALTA",
  "categoria": "DESENVOLVIMENTO",
  "dataInicio": "2024-03-20",
  "dataFim": "2024-03-25",
  "usuario": {
    "id": 1,
    "nome": "João Silva"
  }
}
```

### 4. Atualizar Tarefa

```http
PUT /api/tarefas/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Implementar API REST",
  "descricao": "Desenvolver endpoints para gerenciamento de tarefas",
  "status": "EM_ANDAMENTO",
  "prioridade": "ALTA",
  "categoria": "DESENVOLVIMENTO",
  "dataInicio": "2024-03-20",
  "dataFim": "2024-03-25"
}
```

Resposta:
```json
{
  "id": 1,
  "titulo": "Implementar API REST",
  "descricao": "Desenvolver endpoints para gerenciamento de tarefas",
  "status": "EM_ANDAMENTO",
  "prioridade": "ALTA",
  "categoria": "DESENVOLVIMENTO",
  "dataInicio": "2024-03-20",
  "dataFim": "2024-03-25",
  "usuario": {
    "id": 1,
    "nome": "João Silva"
  }
}
```

### 5. Excluir Tarefa

```http
DELETE /api/tarefas/1
Authorization: Bearer {token}
```

Resposta: 204 No Content

## Disponibilidade

### 1. Criar Disponibilidade

```http
POST /api/disponibilidade
Authorization: Bearer {token}
Content-Type: application/json

{
  "diaSemana": "SEGUNDA",
  "horaInicio": "09:00",
  "horaFim": "17:00"
}
```

Resposta:
```json
{
  "id": 1,
  "diaSemana": "SEGUNDA",
  "horaInicio": "09:00",
  "horaFim": "17:00",
  "usuario": {
    "id": 1,
    "nome": "João Silva"
  }
}
```

### 2. Listar Disponibilidade por Usuário

```http
GET /api/disponibilidade/usuario/1
Authorization: Bearer {token}
```

Resposta:
```json
[
  {
    "id": 1,
    "diaSemana": "SEGUNDA",
    "horaInicio": "09:00",
    "horaFim": "17:00"
  },
  {
    "id": 2,
    "diaSemana": "TERCA",
    "horaInicio": "09:00",
    "horaFim": "17:00"
  }
]
```

### 3. Listar Disponibilidade por Dia

```http
GET /api/disponibilidade/usuario/1/dia/SEGUNDA
Authorization: Bearer {token}
```

Resposta:
```json
[
  {
    "id": 1,
    "diaSemana": "SEGUNDA",
    "horaInicio": "09:00",
    "horaFim": "17:00"
  }
]
```

### 4. Excluir Disponibilidade

```http
DELETE /api/disponibilidade/1
Authorization: Bearer {token}
```

Resposta: 204 No Content

## Códigos de Erro

### 400 Bad Request
```json
{
  "timestamp": "2024-03-20T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Dados inválidos",
  "path": "/api/tarefas"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-03-20T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token inválido ou expirado",
  "path": "/api/tarefas"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-03-20T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Tarefa não encontrada",
  "path": "/api/tarefas/999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-03-20T10:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Já existe uma tarefa neste horário",
  "path": "/api/tarefas"
}
```

## Exemplos de Uso com cURL

### Criar Tarefa
```bash
curl -X POST http://localhost:8080/api/tarefas \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Implementar API REST",
    "descricao": "Desenvolver endpoints para gerenciamento de tarefas",
    "status": "PENDENTE",
    "prioridade": "ALTA",
    "categoria": "DESENVOLVIMENTO",
    "dataInicio": "2024-03-20",
    "dataFim": "2024-03-25"
  }'
```

### Listar Tarefas
```bash
curl -X GET http://localhost:8080/api/tarefas \
  -H "Authorization: Bearer {token}"
```

### Criar Disponibilidade
```bash
curl -X POST http://localhost:8080/api/disponibilidade \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "diaSemana": "SEGUNDA",
    "horaInicio": "09:00",
    "horaFim": "17:00"
  }'
```

## Exemplos de Uso com Postman

1. Importe a coleção do Postman:
   - Abra o Postman
   - Clique em "Import"
   - Cole a URL da coleção

2. Configure as variáveis de ambiente:
   - `base_url`: http://localhost:8080
   - `token`: Token JWT após login

3. Execute as requisições:
   - Registro de usuário
   - Login
   - Criar tarefa
   - Listar tarefas
   - Criar disponibilidade
   - Listar disponibilidade 