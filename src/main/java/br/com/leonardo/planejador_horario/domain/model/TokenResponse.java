package br.com.leonardo.planejador_horario.domain.model;

public class TokenResponse {
    private String token;
    private String tipo;

    public TokenResponse(String token) {
        this.token = token;
        this.tipo = "Bearer";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
} 