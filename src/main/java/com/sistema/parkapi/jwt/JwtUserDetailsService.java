package com.sistema.parkapi.jwt;

import com.sistema.parkapi.entity.Usuario;
import com.sistema.parkapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por carregar os detalhes do usuário para autenticação JWT.
 *
 * Implementa {@link UserDetailsService}, sendo utilizado pelo Spring Security para
 * buscar um usuário com base no nome de usuário (username).
 *
 * Utiliza {@link UsuarioService} para obter informações do usuário da base de dados.
 */
@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    /**
     * Serviço de acesso aos dados dos usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Carrega os detalhes de um usuário com base no nome de usuário.
     *
     * Este método é chamado automaticamente pelo Spring Security durante
     * o processo de autenticação.
     *
     * @param username o nome de usuário fornecido na autenticação.
     * @return um {@link UserDetails} contendo informações do usuário.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorNome(username);
        return new JwtUserDetails(usuario);
    }

    /**
     * Gera um token JWT para um usuário autenticado.
     *
     * O token é criado a partir do nome de usuário e do papel (role) do usuário,
     * removendo o prefixo "ROLE_" da string.
     *
     * @param username o nome de usuário autenticado.
     * @return um {@link JwtToken} com os dados de autenticação.
     */
    public JwtToken getTokenAuthenticated(String username) {
        Usuario.Role role = usuarioService.buscarRolePorUsername(username);
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }
}