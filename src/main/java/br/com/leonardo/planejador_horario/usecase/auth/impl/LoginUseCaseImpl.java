package br.com.leonardo.planejador_horario.usecase.auth.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.LoginRequest;
import br.com.leonardo.planejador_horario.adapter.inbound.dto.TokenResponse;
import br.com.leonardo.planejador_horario.config.security.JwtTokenProvider;
import br.com.leonardo.planejador_horario.usecase.auth.LoginUseCase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public LoginUseCaseImpl(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getSenha()
            )
        );

        String jwt = tokenProvider.generateToken(authentication);
        return new TokenResponse(jwt);
    }
} 