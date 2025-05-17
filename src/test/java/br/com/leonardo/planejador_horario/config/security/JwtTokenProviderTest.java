package br.com.leonardo.planejador_horario.config.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetails userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "testSecretKey123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", 3600000L);

        userDetails = new User("teste@teste.com", "senha123", new ArrayList<>());
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    void deveGerarTokenComSucesso() {
        String token = jwtTokenProvider.generateToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void deveExtrairEmailDoTokenComSucesso() {
        String token = jwtTokenProvider.generateToken(authentication);
        String email = jwtTokenProvider.getUserEmailFromToken(token);

        assertEquals(userDetails.getUsername(), email);
    }

    @Test
    void deveValidarTokenComSucesso() {
        String token = jwtTokenProvider.generateToken(authentication);
        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void deveInvalidarTokenMalFormado() {
        String tokenInvalido = "token_invalido";
        boolean isValid = jwtTokenProvider.validateToken(tokenInvalido);

        assertFalse(isValid);
    }
} 