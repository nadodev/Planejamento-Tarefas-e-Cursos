package br.com.leonardo.planejador_horario.usecase.usuario.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.UsuarioDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.application.port.out.UsuarioRepository;
import br.com.leonardo.planejador_horario.domain.exception.EmailJaCadastradoException;
import br.com.leonardo.planejador_horario.usecase.usuario.AtualizarUsuarioUseCase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AtualizarUsuarioUseCaseImpl implements AtualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AtualizarUsuarioUseCaseImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Optional<UsuarioEntity> atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    if (!usuario.getEmail().equals(usuarioDTO.getEmail()) &&
                            usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
                        throw new EmailJaCadastradoException("Email já cadastrado: " + usuarioDTO.getEmail());
                    }

                    usuario.setNome(usuarioDTO.getNome());
                    usuario.setEmail(usuarioDTO.getEmail());
                    
                    if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
                        usuario.setSenhaHash(passwordEncoder.encode(usuarioDTO.getSenha()));
                    }

                    return usuarioRepository.save(usuario);
                });
    }
} 