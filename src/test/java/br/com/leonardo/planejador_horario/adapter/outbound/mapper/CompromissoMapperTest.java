package br.com.leonardo.planejador_horario.adapter.outbound.mapper;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.domain.model.Compromisso;
import br.com.leonardo.planejador_horario.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CompromissoMapperTest {

    private CompromissoMapper mapper;
    private Usuario usuario;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        mapper = new CompromissoMapper();
        
        usuario = new Usuario(1L, "Teste", "teste@email.com", "senha123");
        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1L);
        usuarioEntity.setNome("Teste");
        usuarioEntity.setEmail("teste@email.com");
        usuarioEntity.setSenhaHash("senha123");
    }

    @Test
    void deveMapearCorretamenteParaEntity() {
        // Arrange
        Compromisso compromisso = new Compromisso(
            1L,
            "Reunião",
            usuario,
            DayOfWeek.MONDAY,
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            true
        );

        // Act
        CompromissoEntity entity = mapper.toEntity(compromisso, usuarioEntity);

        // Assert
        assertNotNull(entity);
        assertEquals(compromisso.getId(), entity.getId());
        assertEquals(compromisso.getTitulo(), entity.getTitulo());
        assertEquals(compromisso.getDiaDaSemana(), entity.getDiaDaSemana());
        assertEquals(compromisso.getHorarioInicio(), entity.getHorarioInicio());
        assertEquals(compromisso.getHorarioFim(), entity.getHorarioFim());
        assertEquals(compromisso.isRecorrente(), entity.isRecorrente());
        assertEquals(compromisso.getUsuario().getId(), entity.getUsuario().getId());
    }

    @Test
    void deveMapearCorretamenteParaDomain() {
        // Arrange
        CompromissoEntity entity = new CompromissoEntity();
        entity.setId(1L);
        entity.setTitulo("Reunião");
        entity.setDiaDaSemana(DayOfWeek.MONDAY);
        entity.setHorarioInicio(LocalTime.of(14, 0));
        entity.setHorarioFim(LocalTime.of(15, 0));
        entity.setRecorrente(true);
        entity.setUsuario(usuarioEntity);

        // Act
        Compromisso domain = mapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getTitulo(), domain.getTitulo());
        assertEquals(entity.getDiaDaSemana(), domain.getDiaDaSemana());
        assertEquals(entity.getHorarioInicio(), domain.getHorarioInicio());
        assertEquals(entity.getHorarioFim(), domain.getHorarioFim());
        assertEquals(entity.isRecorrente(), domain.isRecorrente());
        assertEquals(entity.getUsuario().getId(), domain.getUsuario().getId());
    }

    @Test
    void deveMapearCorretamenteParaDTO() {
        // Arrange
        Compromisso domain = new Compromisso(
            1L,
            "Reunião",
            usuario,
            DayOfWeek.MONDAY,
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            true
        );

        // Act
        CompromissoDTO dto = mapper.toDTO(domain);

        // Assert
        assertNotNull(dto);
        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getTitulo(), dto.getTitulo());
        assertEquals(domain.getDiaDaSemana(), dto.getDiaDaSemana());
        assertEquals(domain.getHorarioInicio(), dto.getHorarioInicio());
        assertEquals(domain.getHorarioFim(), dto.getHorarioFim());
        assertEquals(domain.isRecorrente(), dto.isRecorrente());
        assertEquals(domain.getUsuario().getId(), dto.getUsuarioId());
    }

    @Test
    void deveMapearCorretamenteDTOParaDomain() {
        // Arrange
        CompromissoDTO dto = new CompromissoDTO(
            1L,
            "Reunião",
            DayOfWeek.MONDAY,
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            true,
            1L
        );

        // Act
        Compromisso domain = mapper.toDomain(dto, usuario);

        // Assert
        assertNotNull(domain);
        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getTitulo(), domain.getTitulo());
        assertEquals(dto.getDiaDaSemana(), domain.getDiaDaSemana());
        assertEquals(dto.getHorarioInicio(), domain.getHorarioInicio());
        assertEquals(dto.getHorarioFim(), domain.getHorarioFim());
        assertEquals(dto.isRecorrente(), domain.isRecorrente());
        assertEquals(usuario.getId(), domain.getUsuario().getId());
    }
} 