package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.domain.model.Curso;
import br.com.leonardo.planejador_horario.domain.model.Usuario;
import br.com.leonardo.planejador_horario.usecase.curso.CriarCursoUseCase;
import br.com.leonardo.planejador_horario.usecase.curso.DeletaCursoUseCase;
import br.com.leonardo.planejador_horario.usecase.curso.ListarCursosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Cursos", description = "API para gerenciamento de cursos")
@RequestMapping("/api/cursos")
public class CursoController {

    private final CriarCursoUseCase criarCursoUseCase;
    private final ListarCursosUseCase listarCursosUseCase;
    private final DeletaCursoUseCase deletarCursoUseCase;

    public CursoController(
            CriarCursoUseCase criarCursoUseCase,
            ListarCursosUseCase listarCursosUseCase,
            @Qualifier("deletarCursoUsoCaseImpl") DeletaCursoUseCase deletarCursoUseCase
    ) {
        this.criarCursoUseCase = criarCursoUseCase;
        this.listarCursosUseCase = listarCursosUseCase;
        this.deletarCursoUseCase = deletarCursoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> criarCurso(
        @Valid @RequestBody CursoDTO request
    ) {
        try {
            CursoDTO cursoCriado = criarCursoUseCase.criar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoCriado);
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao processar requisição: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        List<CursoDTO> cursos = listarCursosUseCase.listarCurso()
                .stream()
                .map(CursoDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CursoDTO>> listarCursos(
        @Parameter(description = "ID do usuário", required = true, example = "1")
        @PathVariable Long usuarioId
    ) {
        List<CursoDTO> cursos = listarCursosUseCase.listarPorUsuario(usuarioId).stream()
                .map(CursoDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(cursos);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletar(
        @Parameter(description = "ID do curso", required = true, example = "1")
        @PathVariable Long id
    ) {
        deletarCursoUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
