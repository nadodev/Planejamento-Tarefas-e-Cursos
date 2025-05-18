package br.com.leonardo.planejador_horario.domain.service;

import br.com.leonardo.planejador_horario.domain.entities.Disponibilidade;
import br.com.leonardo.planejador_horario.domain.entities.Usuario;
import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import br.com.leonardo.planejador_horario.domain.exception.DisponibilidadeNaoEncontradaException;
import br.com.leonardo.planejador_horario.domain.repository.DisponibilidadeRepository;
import br.com.leonardo.planejador_horario.domain.repository.UsuarioRepository;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeRequest;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisponibilidadeServiceTest {

    @Mock
    private DisponibilidadeRepository disponibilidadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DisponibilidadeService disponibilidadeService;

    private Usuario usuario;
    private Disponibilidade disponibilidade;
    private DisponibilidadeRequest request;
    private DisponibilidadeResponse response;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");

        disponibilidade = new Disponibilidade();
        disponibilidade.setId(1L);
        disponibilidade.setDiaSemana(DiaSemana.SEGUNDA);
        disponibilidade.setHoraInicio(LocalTime.of(9, 0));
        disponibilidade.setHoraFim(LocalTime.of(17, 0));
        disponibilidade.setUsuario(usuario);

        request = new DisponibilidadeRequest();
        request.setDiaSemana(DiaSemana.SEGUNDA);
        request.setHoraInicio(LocalTime.of(9, 0));
        request.setHoraFim(LocalTime.of(17, 0));

        response = new DisponibilidadeResponse();
        response.setId(1L);
        response.setDiaSemana(DiaSemana.SEGUNDA);
        response.setHoraInicio(LocalTime.of(9, 0));
        response.setHoraFim(LocalTime.of(17, 0));
    }

    @Test
    void criar_DeveRetornarDisponibilidadeCriada() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(disponibilidadeRepository.save(any(Disponibilidade.class))).thenReturn(disponibilidade);

        DisponibilidadeResponse result = disponibilidadeService.criar(1L, request);

        assertNotNull(result);
        assertEquals(request.getDiaSemana(), result.getDiaSemana());
        assertEquals(request.getHoraInicio(), result.getHoraInicio());
        assertEquals(request.getHoraFim(), result.getHoraFim());
        verify(disponibilidadeRepository).save(any(Disponibilidade.class));
    }

    @Test
    void listarPorUsuario_DeveRetornarLista() {
        when(disponibilidadeRepository.findByUsuarioId(anyLong())).thenReturn(Arrays.asList(disponibilidade));

        List<DisponibilidadeResponse> result = disponibilidadeService.listarPorUsuario(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(disponibilidadeRepository).findByUsuarioId(1L);
    }

    @Test
    void listarPorUsuarioEDia_DeveRetornarLista() {
        when(disponibilidadeRepository.findByUsuarioIdAndDiaSemana(anyLong(), any(DiaSemana.class)))
                .thenReturn(Arrays.asList(disponibilidade));

        List<DisponibilidadeResponse> result = disponibilidadeService.listarPorUsuarioEDia(1L, DiaSemana.SEGUNDA);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(disponibilidadeRepository).findByUsuarioIdAndDiaSemana(1L, DiaSemana.SEGUNDA);
    }

    @Test
    void excluir_DeveExcluirDisponibilidade() {
        when(disponibilidadeRepository.findById(anyLong())).thenReturn(Optional.of(disponibilidade));
        doNothing().when(disponibilidadeRepository).delete(any(Disponibilidade.class));

        disponibilidadeService.excluir(1L);

        verify(disponibilidadeRepository).delete(disponibilidade);
    }

    @Test
    void excluir_Inexistente_DeveLancarExcecao() {
        when(disponibilidadeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DisponibilidadeNaoEncontradaException.class, () -> disponibilidadeService.excluir(1L));
    }
} 