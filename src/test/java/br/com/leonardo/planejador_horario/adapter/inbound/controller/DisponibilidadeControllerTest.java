package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeRequest;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeResponse;
import br.com.leonardo.planejador_horario.services.DisponibilidadeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DisponibilidadeControllerTest {

    @Mock
    private DisponibilidadeService disponibilidadeService;

    @InjectMocks
    private DisponibilidadeController disponibilidadeController;

    private DisponibilidadeRequest request;
    private DisponibilidadeResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
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
        when(disponibilidadeService.criar(anyLong(), any(DisponibilidadeRequest.class))).thenReturn(response);

        ResponseEntity<DisponibilidadeResponse> result = disponibilidadeController.criar(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(disponibilidadeService).criar(1L, request);
    }

    @Test
    void listarPorUsuario_DeveRetornarLista() {
        List<DisponibilidadeResponse> disponibilidades = Arrays.asList(response);
        when(disponibilidadeService.listarPorUsuario(anyLong())).thenReturn(disponibilidades);

        ResponseEntity<List<DisponibilidadeResponse>> result = disponibilidadeController.listarPorUsuario(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(disponibilidades, result.getBody());
        verify(disponibilidadeService).listarPorUsuario(1L);
    }

    @Test
    void listarPorUsuarioEDia_DeveRetornarLista() {
        List<DisponibilidadeResponse> disponibilidades = Arrays.asList(response);
        when(disponibilidadeService.listarPorUsuarioEDia(anyLong(), any(DiaSemana.class))).thenReturn(disponibilidades);

        ResponseEntity<List<DisponibilidadeResponse>> result = disponibilidadeController.listarPorUsuarioEDia(1L, DiaSemana.SEGUNDA);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(disponibilidades, result.getBody());
        verify(disponibilidadeService).listarPorUsuarioEDia(1L, DiaSemana.SEGUNDA);
    }

    @Test
    void excluir_DeveRetornarNoContent() {
        doNothing().when(disponibilidadeService).excluir(anyLong());

        ResponseEntity<Void> result = disponibilidadeController.excluir(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(disponibilidadeService).excluir(1L);
    }
} 