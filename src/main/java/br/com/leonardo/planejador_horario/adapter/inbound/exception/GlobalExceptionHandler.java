package br.com.leonardo.planejador_horario.adapter.inbound.exception;

import br.com.leonardo.planejador_horario.domain.exception.CursoException;
import br.com.leonardo.planejador_horario.domain.exception.CursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleCursoNaoEncontrado(CursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "timestamp", ZonedDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "Curso não encontrado",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "timestamp", ZonedDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "Usuário não encontrado",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of(
                        "timestamp", ZonedDateTime.now(),
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "error", "Credenciais inválidas",
                        "message", "Email ou senha incorretos"
                )
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "timestamp", ZonedDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "Usuário não encontrado",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of(
                        "timestamp", ZonedDateTime.now(),
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "error", "Erro de autenticação",
                        "message", "Falha na autenticação: " + ex.getMessage()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "timestamp", ZonedDateTime.now(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", "Erro interno",
                        "message", "Algo deu errado. Tente novamente mais tarde."
                )
        );
    }
}
