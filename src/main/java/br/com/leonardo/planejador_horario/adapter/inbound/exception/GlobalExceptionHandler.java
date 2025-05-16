package br.com.leonardo.planejador_horario.adapter.inbound.exception;

import br.com.leonardo.planejador_horario.domain.exception.CursoException;
import br.com.leonardo.planejador_horario.domain.exception.CursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CursoException.class)
    public ResponseEntity<ErrorResponse> handleCursoException(CursoException ex) {
        HttpStatus status = ex instanceof CursoNaoEncontradoException
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(new ErrorResponse(ex.getMessage()));
    }

    public static class ErrorResponse {
        private String message;
        private LocalDateTime timestamp;
        private String path;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = LocalDateTime.now();
        }
    }
}