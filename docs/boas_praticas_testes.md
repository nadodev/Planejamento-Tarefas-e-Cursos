# Boas Práticas de Testes em Spring Boot

## 1. Princípios Fundamentais

### 1.1 F.I.R.S.T
- **F**ast (Rápido): Testes devem executar rapidamente
- **I**ndependent (Independente): Cada teste deve ser independente dos outros
- **R**epeatable (Repetível): Resultados consistentes em qualquer ambiente
- **S**elf-validating (Auto-validável): Passa ou falha, sem interpretação manual
- **T**imely (Oportuno): Escrito junto ou antes do código de produção

### 1.2 Padrão AAA (Arrange-Act-Assert)
```java
@Test
void exemploTeste() {
    // Arrange (Preparação)
    var curso = new CursoDTO();
    curso.setNome("Teste");

    // Act (Execução)
    var resultado = service.criar(curso);

    // Assert (Verificação)
    assertNotNull(resultado);
}
```

## 2. Estrutura de Testes

### 2.1 Nomenclatura
```java
// Padrão: deve[Resultado]Quando[Condição]
@Test
void deveRetornarErroQuandoNomeVazio() { }

// Padrão: deve[Ação]Com[Condição]
@Test
void deveSalvarCursoComDadosValidos() { }
```

### 2.2 Organização de Pacotes
```
src/
└── test/
    └── java/
        └── br.com.exemplo/
            ├── unit/          # Testes unitários
            ├── integration/   # Testes de integração
            └── util/          # Classes utilitárias para testes
```

## 3. Tipos de Teste em Detalhe

### 3.1 Testes Unitários
```java
@ExtendWith(MockitoExtension.class)
class ServicoTest {
    @Mock
    private Repositorio repo;  // Mock das dependências

    @InjectMocks
    private Servico servico;   // Classe sendo testada

    @Test
    void testeUnitario() {
        // Configura comportamento do mock
        when(repo.buscar()).thenReturn(valor);

        // Executa e verifica
        var resultado = servico.processar();
        assertEquals(esperado, resultado);
    }
}
```

### 3.2 Testes de Integração
```java
@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testeIntegracao() {
        mockMvc.perform(get("/api/recurso"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.campo").value(valor));
    }
}
```

## 4. Mocks e Stubs

### 4.1 Quando Usar
- **Mock**: Verifica interações/comportamento
- **Stub**: Fornece dados para o teste

### 4.2 Exemplos
```java
// Mock para verificar comportamento
verify(repository).save(any());

// Stub para fornecer dados
when(service.buscarDados()).thenReturn(dadosTeste);
```

## 5. Testes de API REST

### 5.1 Testando Endpoints
```java
mockMvc.perform(post("/api/cursos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists());
```

### 5.2 Validando Respostas
```java
// Validando corpo da resposta
.andExpect(jsonPath("$.nome").value("Curso"))
.andExpect(jsonPath("$.items").isArray())
.andExpect(jsonPath("$.items", hasSize(2)));
```

## 6. Testes de Banco de Dados

### 6.1 Configuração
```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

### 6.2 Limpeza de Dados
```java
@BeforeEach
void setUp() {
    repository.deleteAll();
    // Prepara dados de teste
}
```

## 7. Dicas Importantes

### 7.1 Dados de Teste
- Use builders ou factories para criar dados de teste
- Mantenha dados de teste realistas
- Evite dados aleatórios

### 7.2 Assertions
- Use assertions expressivas
- Prefira AssertJ para coleções
- Verifique exceções quando esperadas

```java
// Verificando exceções
assertThrows(UsuarioNaoEncontradoException.class, 
    () -> service.buscarUsuario(999L));

// Verificando coleções
assertThat(lista)
    .hasSize(3)
    .contains(elemento)
    .doesNotContain(outroElemento);
```

### 7.3 Testes Parametrizados
```java
@ParameterizedTest
@CsvSource({
    "nome1, email1@teste.com",
    "nome2, email2@teste.com"
})
void testeComVariosValores(String nome, String email) {
    // Teste com diferentes valores
}
```

## 8. Cobertura de Testes

### 8.1 Métricas
- Cobertura de linhas: > 80%
- Cobertura de branches: > 70%
- Foco em código crítico

### 8.2 Ferramentas
- JaCoCo para relatórios de cobertura
- SonarQube para análise de qualidade

## 9. Troubleshooting Comum

### 9.1 Testes Instáveis
- Verifique dependências entre testes
- Use @DirtiesContext quando necessário
- Limpe dados após cada teste

### 9.2 Testes Lentos
- Minimize uso de @SpringBootTest
- Use @WebMvcTest para controllers
- Use @DataJpaTest para repositórios

## 10. Checklist de Revisão

- [ ] Testes seguem padrão F.I.R.S.T
- [ ] Nomenclatura clara e descritiva
- [ ] Uso apropriado de mocks
- [ ] Assertions específicas e claras
- [ ] Dados de teste bem definidos
- [ ] Limpeza adequada do contexto
- [ ] Cobertura adequada
- [ ] Documentação quando necessária 