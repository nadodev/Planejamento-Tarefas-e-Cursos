package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import io.swagger.v3.oas.annotations.media.Schema;

class ErrorResponse {
    @Schema(description = "Timestamp do erro", example = "2024-01-01T10:00:00Z")
    private String timestamp;

    @Schema(description = "Status HTTP", example = "400")
    private int status;

    @Schema(description = "Tipo do erro", example = "Erro de validação")
    private String error;

    @Schema(description = "Mensagem de erro", example = "Campo título é obrigatório")
    private String message;
}
