# Documentação de Testes - Módulo de Cursos

## Visão Geral

O módulo de cursos possui dois tipos de testes:

1. **Testes Unitários** (`CursoControllerTest`)
   - Focados em testar o comportamento do controller isoladamente
   - Utilizam mocks para simular as dependências
   - Mais rápidos e não requerem banco de dados

2. **Testes de Integração** (`CursoControllerIntegrationTest`)
   - Testam o fluxo completo da aplicação
   - Utilizam banco de dados H2 em memória
   - Validam a integração entre os componentes

## Configuração do Ambiente de Teste

### Banco de Dados de Teste

O ambiente de teste utiliza um banco H2 em memória, configurado em `application-test.properties`:

```properties
# Configuração do banco de dados em memória para testes
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

# Configuração do JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

# Configuração para executar o schema.sql
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
```

### Estrutura do Banco

O esquema do banco é definido em `schema.sql`:

```sql
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    carga_horaria INTEGER NOT NULL,
    prioridade INTEGER NOT NULL,
    prazo_final DATE,
    usuario_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

## Testes Unitários (CursoControllerTest)

### 1. Criar Curso

```java
@Test
@DisplayName("Deve criar um curso com sucesso")
void deveCriarCursoComSucesso()
```
- **Objetivo**: Validar a criação de um novo curso
- **Cenário**: Dados válidos de curso e usuário existente
- **Resultado Esperado**: Status 201 (CREATED) e curso criado

### 2. Validar Usuário Inexistente

```java
@Test
@DisplayName("Deve retornar erro quando usuário não encontrado")
void deveRetornarErroQuandoUsuarioNaoEncontrado()
```
- **Objetivo**: Validar tentativa de criar curso com usuário inexistente
- **Cenário**: ID de usuário inválido
- **Resultado Esperado**: Status 404 (NOT FOUND)

### 3. Listar Cursos

```java
@Test
@DisplayName("Deve listar todos os cursos com sucesso")
void deveListarTodosCursosComSucesso()
```
- **Objetivo**: Validar listagem de todos os cursos
- **Cenário**: Base com cursos cadastrados
- **Resultado Esperado**: Status 200 (OK) e lista de cursos

### 4. Listar por Usuário

```java
@Test
@DisplayName("Deve listar cursos por usuário com sucesso")
void deveListarCursosPorUsuarioComSucesso()
```
- **Objetivo**: Validar listagem de cursos por usuário
- **Cenário**: Usuário com cursos cadastrados
- **Resultado Esperado**: Status 200 (OK) e lista filtrada

### 5. Deletar Curso

```java
@Test
@DisplayName("Deve deletar curso com sucesso")
void deveDeletarCursoComSucesso()
```
- **Objetivo**: Validar exclusão de curso
- **Cenário**: Curso existente
- **Resultado Esperado**: Status 204 (NO CONTENT)

## Testes de Integração (CursoControllerIntegrationTest)

Os testes de integração validam o mesmo conjunto de funcionalidades, mas com algumas diferenças:

1. **Setup**:
   - Limpa o banco antes de cada teste
   - Cria usuário de teste real
   - Utiliza transações reais

2. **Validações Adicionais**:
   - Verifica persistência real no banco
   - Valida integridade referencial
   - Testa comportamento completo da aplicação

## Como Executar os Testes

### Testes Unitários
```bash
mvn test -Dtest=CursoControllerTest
```

### Testes de Integração
```bash
mvn test -Dtest=CursoControllerIntegrationTest
```

### Todos os Testes
```bash
mvn test
```

## Boas Práticas Implementadas

1. **Isolamento**: Cada teste é independente e limpa seus dados
2. **Nomenclatura**: Nomes descritivos que indicam o cenário testado
3. **Organização**: Padrão Arrange/Act/Assert nos métodos
4. **Documentação**: Uso de @DisplayName para descrição clara
5. **Transações**: Uso de @Transactional para garantir rollback 