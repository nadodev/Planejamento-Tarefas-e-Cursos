package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.UsuarioDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.usecase.usuario.AtualizarUsuarioUseCase;
import br.com.leonardo.planejador_horario.usecase.usuario.CriarUsuarioUseCase;
import br.com.leonardo.planejador_horario.usecase.usuario.DeletarUsuarioUseCase;
import br.com.leonardo.planejador_horario.usecase.usuario.ListarUsuariosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;

    public UsuarioController(
            CriarUsuarioUseCase criarUsuarioUseCase,
            ListarUsuariosUseCase listarUsuariosUseCase,
            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
            DeletarUsuarioUseCase deletarUsuarioUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
    }

    @Operation(summary = "Criar um novo usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                content = @Content(schema = @Schema(implementation = UsuarioEntity.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já cadastrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioEntity> criarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do usuário a ser criado", required = true)
            @RequestBody @Valid UsuarioDTO usuarioDTO) {
        UsuarioEntity usuario = criarUsuarioUseCase.criarUsuario(usuarioDTO);
        return ResponseEntity.status(201).body(usuario);
    }

    @Operation(summary = "Listar todos os usuários")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioEntity.class)))
    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listarUsuarios() {
        List<UsuarioEntity> usuarios = listarUsuariosUseCase.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                content = @Content(schema = @Schema(implementation = UsuarioEntity.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> buscarPorId(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id) {
        return listarUsuariosUseCase.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                content = @Content(schema = @Schema(implementation = UsuarioEntity.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já cadastrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> atualizarUsuario(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do usuário a ser atualizado", required = true)
            @RequestBody @Valid UsuarioDTO usuarioDTO) {
        return atualizarUsuarioUseCase.atualizarUsuario(id, usuarioDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deletar um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id) {
        deletarUsuarioUseCase.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
} 