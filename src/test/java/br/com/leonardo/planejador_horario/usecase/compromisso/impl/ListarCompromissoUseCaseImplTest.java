package br.com.leonardo.planejador_horario.usecase.compromisso.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.mapper.CompromissoMapper;
import br.com.leonardo.planejador_horario.application.port.out.CompromissoRepository;
import br.com.leonardo.planejador_horario.domain.exception.CompromissoNaoEncontradoException;
import br.com.leonardo.planejador_horario.domain.model.Compromisso;
import br.com.leonardo.planejador_horario.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarCompromissoUseCaseImplTest {

    @Mock
    private CompromissoRepository compromissoRepository;

    @Mock
    private CompromissoMapper compromissoMapper;

    private ListarCompromissoUseCaseImpl useCase;
    private CompromissoEntity compromissoEntity;
    private Compromisso compromissoDomain;
    private CompromissoDTO compromissoDTO;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        useCase = new ListarCompromissoUseCaseImpl(compromissoRepository, compromissoMapper);

        // Configurar dados de teste
        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1L);
        usuarioEntity.setNome("Teste");
        usuarioEntity.setEmail("teste@email.com");

        compromissoEntity = new CompromissoEntity();
        compromissoEntity.setId(1L);
        compromissoEntity.setTitulo("Reunião");
        compromissoEntity.setDiaDaSemana(DayOfWeek.MONDAY);
        compromissoEntity.setHorarioInicio(LocalTime.of(14, 0));
        compromissoEntity.setHorarioFim(LocalTime.of(15, 0));
        compromissoEntity.setRecorrente(true);
        compromissoEntity.setUsuario(usuarioEntity);

        Usuario usuario = new Usuario(1L, "Teste", "teste@email.com", "senha123");
        compromissoDomain = new Compromisso(1L, "Reunião", usuario, DayOfWeek.MONDAY,
                LocalTime.of(14, 0), LocalTime.of(15, 0), true);

        compromissoDTO = new CompromissoDTO(1L, "Reunião", DayOfWeek.MONDAY,
                LocalTime.of(14, 0), LocalTime.of(15, 0), true, 1L);
    }

    @Test
    void deveListarTodosCompromissosComSucesso() {
        // Arrange
        when(compromissoRepository.findAll()).thenReturn(Arrays.asList(compromissoEntity));
        when(compromissoMapper.toDomain(compromissoEntity)).thenReturn(compromissoDomain);
        when(compromissoMapper.toDTO(compromissoDomain)).thenReturn(compromissoDTO);

        // Act
        List<CompromissoDTO> resultado = useCase.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(compromissoDTO, resultado.get(0));
        verify(compromissoRepository).findAll();
        verify(compromissoMapper).toDomain(compromissoEntity);
        verify(compromissoMapper).toDTO(compromissoDomain);
    }

    @Test
    void deveListarCompromissosPorUsuarioComSucesso() {
        // Arrange
        Long usuarioId = 1L;
        when(compromissoRepository.findByUsuarioId(usuarioId)).thenReturn(Arrays.asList(compromissoEntity));
        when(compromissoMapper.toDomain(compromissoEntity)).thenReturn(compromissoDomain);
        when(compromissoMapper.toDTO(compromissoDomain)).thenReturn(compromissoDTO);

        // Act
        List<CompromissoDTO> resultado = useCase.listarPorUsuario(usuarioId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(compromissoDTO, resultado.get(0));
        verify(compromissoRepository).findByUsuarioId(usuarioId);
        verify(compromissoMapper).toDomain(compromissoEntity);
        verify(compromissoMapper).toDTO(compromissoDomain);
    }

    @Test
    void deveBuscarCompromissoPorIdComSucesso() {
        // Arrange
        Long id = 1L;
        when(compromissoRepository.findById(id)).thenReturn(Optional.of(compromissoEntity));
        when(compromissoMapper.toDomain(compromissoEntity)).thenReturn(compromissoDomain);
        when(compromissoMapper.toDTO(compromissoDomain)).thenReturn(compromissoDTO);

        // Act
        CompromissoDTO resultado = useCase.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(compromissoDTO, resultado);
        verify(compromissoRepository).findById(id);
        verify(compromissoMapper).toDomain(compromissoEntity);
        verify(compromissoMapper).toDTO(compromissoDomain);
    }

    @Test
    void deveLancarExcecaoQuandoCompromissoNaoEncontrado() {
        // Arrange
        Long id = 1L;
        when(compromissoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompromissoNaoEncontradoException.class, () -> useCase.buscarPorId(id));
        verify(compromissoRepository).findById(id);
        verifyNoInteractions(compromissoMapper);
    }
} 