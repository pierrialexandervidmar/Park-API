package com.sistema.parkapi.service;

import com.sistema.parkapi.entity.Usuario;
import com.sistema.parkapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    @Transactional
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscaPorId(Long id) {
        Optional<Usuario> obj = usuarioRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        // Validamos se a nova senha e confirmação são diferentes ou iguais
        if(!novaSenha.equals(confirmaSenha)) {
            throw new RuntimeException("Nova senha não confere com confirmação de senha");
        }

        Usuario user = buscaPorId(id);

        // Validamos se a nova senha e confirmação são diferentes ou iguais
        if(!user.getPassword().equals(senhaAtual)) {
            throw new RuntimeException("Senha atual não confere");
        }

        user.setPassword(novaSenha);
        return user;
    }

    @Transactional(readOnly = true)
	public List<Usuario> buscarTodos() {
		return usuarioRepository.findAll();
	}
}
