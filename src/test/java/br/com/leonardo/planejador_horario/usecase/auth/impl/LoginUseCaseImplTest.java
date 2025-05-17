package br.com.leonardo.planejador_horario.usecase.auth.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.LoginRequest;
import br.com.leonardo.planejador_horario.adapter.inbound.dto.TokenResponse;
import br.com.leonardo.planejador_horario.config.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    private LoginUseCaseImpl loginUseCase;
    private LoginRequest loginRequest;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        loginUseCase = new LoginUseCaseImpl(authenticationManager, tokenProvider);
        loginRequest = new LoginRequest();
        loginRequest.setEmail("teste@teste.com");
        loginRequest.setSenha("senha123");

        UserDetails userDetails = new User(loginRequest.getEmail(), loginRequest.getSenha(), new ArrayList<>());
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    void deveRealizarLoginComSucesso() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("token_jwt");

        TokenResponse response = loginUseCase.login(loginRequest);

        assertNotNull(response);
        assertEquals("token_jwt", response.getToken());
        assertEquals("Bearer", response.getTipo());
    }

    @Test
    void deveLancarExcecaoQuandoCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(BadCredentialsException.class, () -> loginUseCase.login(loginRequest));
    }
} 