package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.TarefaDTO;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Prioridade;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Status;
import br.com.leonardo.planejador_horario.domain.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
@Tag(name = "Tarefas", description = "Operações relacionadas a tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    @Operation(summary = "Criar uma nova tarefa", description = "Cria uma nova tarefa para um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<TarefaDTO> criarTarefa(
        @Valid @RequestBody TarefaDTO tarefaDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(tarefaService.criarTarefa(tarefaDTO));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar tarefas de um usuário", description = "Retorna uma lista paginada de tarefas de um usuário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Page<TarefaDTO>> listarTarefas(
        @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId,
        @Parameter(description = "Parâmetros de paginação (page, size, sort)") Pageable pageable
    ) {
        return ResponseEntity.ok(tarefaService.listarTarefas(usuarioId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID", description = "Retorna uma tarefa específica pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TarefaDTO> buscarTarefaPorId(
        @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(tarefaService.buscarTarefaPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma tarefa", description = "Atualiza os dados de uma tarefa existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<TarefaDTO> atualizarTarefa(
        @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id,
        @Valid @RequestBody TarefaDTO tarefaDTO
    ) {
        return ResponseEntity.ok(tarefaService.atualizarTarefa(id, tarefaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma tarefa", description = "Remove uma tarefa do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirTarefa(
        @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id
    ) {
        tarefaService.excluirTarefa(id);
    }

    @GetMapping("/usuario/{usuarioId}/status/{status}")
    @Operation(
        summary = "Buscar tarefas por status",
        description = """
            Retorna todas as tarefas de um usuário com um status específico.
            
            Status disponíveis:
            - PENDENTE: Tarefa ainda não iniciada
            - EM_ANDAMENTO: Tarefa em execução
            - CONCLUIDA: Tarefa finalizada
            - CANCELADA: Tarefa cancelada
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<TarefaDTO>> buscarTarefasPorStatus(
        @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId,
        @Parameter(
            description = "Status da tarefa",
            schema = @Schema(
                implementation = Status.class,
                allowableValues = {"PENDENTE", "EM_ANDAMENTO", "CONCLUIDA", "CANCELADA"}
            )
        ) @PathVariable Status status
    ) {
        return ResponseEntity.ok(tarefaService.buscarTarefasPorStatus(usuarioId, status));
    }

    @GetMapping("/usuario/{usuarioId}/prioridade/{prioridade}")
    @Operation(
        summary = "Buscar tarefas por prioridade",
        description = """
            Retorna todas as tarefas de um usuário com uma prioridade específica.
            
            Prioridades disponíveis:
            - BAIXA: Prioridade baixa
            - MEDIA: Prioridade média
            - ALTA: Prioridade alta
            - URGENTE: Prioridade urgente
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<TarefaDTO>> buscarTarefasPorPrioridade(
        @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId,
        @Parameter(
            description = "Prioridade da tarefa",
            schema = @Schema(
                implementation = Prioridade.class,
                allowableValues = {"BAIXA", "MEDIA", "ALTA", "URGENTE"}
            )
        ) @PathVariable Prioridade prioridade
    ) {
        return ResponseEntity.ok(tarefaService.buscarTarefasPorPrioridade(usuarioId, prioridade));
    }

    @GetMapping("/usuario/{usuarioId}/categoria/{categoria}")
    @Operation(summary = "Buscar tarefas por categoria", description = "Retorna todas as tarefas de um usuário de uma categoria específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<TarefaDTO>> buscarTarefasPorCategoria(
        @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId,
        @Parameter(description = "Categoria da tarefa", example = "ESTUDOS") @PathVariable String categoria
    ) {
        return ResponseEntity.ok(tarefaService.buscarTarefasPorCategoria(usuarioId, categoria));
    }

    @GetMapping("/usuario/{usuarioId}/periodo")
    @Operation(summary = "Buscar tarefas por período", description = "Retorna todas as tarefas de um usuário dentro de um período específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Período inválido")
    })
    public ResponseEntity<List<TarefaDTO>> buscarTarefasPorPeriodo(
        @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId,
        @Parameter(description = "Data inicial (yyyy-MM-dd)", example = "2024-05-18") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
        @Parameter(description = "Data final (yyyy-MM-dd)", example = "2024-05-25") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        return ResponseEntity.ok(tarefaService.buscarTarefasPorPeriodo(usuarioId, dataInicio, dataFim));
    }

    @GetMapping("/usuario/{usuarioId}/atrasadas")
    @Operation(summary = "Buscar tarefas atrasadas", description = "Retorna todas as tarefas atrasadas de um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<TarefaDTO>> buscarTarefasAtrasadas(
        @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(tarefaService.buscarTarefasAtrasadas(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/hoje")
    @Operation(summary = "Buscar tarefas para hoje", description = "Retorna todas as tarefas de um usuário para o dia atual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<TarefaDTO>> buscarTarefasParaHoje(
        @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(tarefaService.buscarTarefasParaHoje(usuarioId));
    }
} 