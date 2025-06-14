package com.sistema.parkapi.jwt;

import com.sistema.parkapi.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Classe que representa os detalhes de autenticação de um usuário com JWT.
 *
 * Estende a classe {@link org.springframework.security.core.userdetails.User}
 * para incorporar um objeto {@link Usuario}, permitindo acesso a informações
 * adicionais do usuário no contexto de segurança.
 */
public class JwtUserDetails extends User {

    /**
     * Instância de {@link Usuario} associada ao usuário autenticado.
     */
    private Usuario usuario;

    /**
     * Construtor da classe.
     *
     * @param usuario objeto do tipo {@link Usuario} que contém as informações do usuário.
     *                É utilizado para inicializar o nome de usuário, senha e autoridade.
     */
    public JwtUserDetails(Usuario usuario) {
        super(
                usuario.getUsername(),
                usuario.getPassword(),
                AuthorityUtils.createAuthorityList(usuario.getRole().name())
        );
        this.usuario = usuario;
    }

    /**
     * Retorna o ID do usuário.
     *
     * @return o identificador único do {@link Usuario}.
     */
    public Long getId() {
        return this.usuario.getId();
    }

    /**
     * Retorna o papel (role) do usuário.
     *
     * @return a role associada ao {@link Usuario} como uma {@link String}.
     */
    public String getRole() {
        return this.usuario.getRole().name();
    }

}

