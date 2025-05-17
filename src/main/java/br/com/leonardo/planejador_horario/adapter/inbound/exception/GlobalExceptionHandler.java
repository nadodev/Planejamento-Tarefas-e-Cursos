package br.com.leonardo.planejador_horario.adapter.inbound.exception;

import br.com.leonardo.planejador_horario.domain.exception.CursoException;
import br.com.leonardo.planejador_horario.domain.exception.CursoNaoEncontradoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
                        "message", "Usuário não encontrado com o email fornecido"
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
                        "message", "Falha na autenticação. Por favor, verifique suas credenciais."
                )
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "timestamp", ZonedDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Erro de integridade de dados",
                        "message", "Não foi possível completar a operação devido a uma violação de integridade de dados"
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", ZonedDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Erro de validação");
        response.put("message", "Existem campos inválidos na requisição");
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "timestamp", ZonedDateTime.now(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", "Erro interno",
                        "message", "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde."
                )
        );
    }
}
