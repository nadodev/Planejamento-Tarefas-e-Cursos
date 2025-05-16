package br.com.leonardo.planejador_horario.adapter.inbound.controller;


import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.domain.model.Curso;
import br.com.leonardo.planejador_horario.domain.model.Usuario;
import br.com.leonardo.planejador_horario.usecase.curso.CriarCursoUseCase;
import br.com.leonardo.planejador_horario.usecase.curso.ListarCursosUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CriarCursoUseCase criarCursoUseCase;
    private final ListarCursosUseCase listarCursosUseCase;

    public CursoController(CriarCursoUseCase criarCursoUseCase, ListarCursosUseCase listarCursosUseCase) {
        this.criarCursoUseCase = criarCursoUseCase;
        this.listarCursosUseCase = listarCursosUseCase;
    }

    @PostMapping
    public ResponseEntity<?> criarCurso(@Valid @RequestBody CursoDTO request) {
        try {
            Curso cursoCriado = criarCursoUseCase.criar(request); // delega tudo
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoCriado);

        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao processar requisição: " + e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Optional<Usuario>> listarCursos(@PathVariable Long usuarioId) {
        Optional<Usuario> cursos = listarCursosUseCase.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(cursos);
    }
}
