package br.com.leonardo.planejador_horario.usecase.auth;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.LoginRequest;
import br.com.leonardo.planejador_horario.adapter.inbound.dto.TokenResponse;

public interface LoginUseCase {
    TokenResponse login(LoginRequest loginRequest);
} 