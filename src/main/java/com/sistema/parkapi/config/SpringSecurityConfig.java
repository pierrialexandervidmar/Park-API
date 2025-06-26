package com.sistema.parkapi.config;

import com.sistema.parkapi.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class SpringSecurityConfig {

    /**
     * Configura o filtro de segurança para a aplicação, definindo regras para autenticação e autorização.
     * <p>
     * Este metodo desativa a proteção CSRF, o login via formulário, e a autenticação HTTP básica.
     * Ele também define as permissões de acesso para diferentes requisições HTTP:
     * <p>
     * - A requisição POST para "api/v1/usuarios" será permitida para todos os usuários.
     * - Todas as outras requisições exigem autenticação.
     * <p>
     * Além disso, a política de criação de sessões é configurada para **stateless**, o que significa que a aplicação
     * não manterá informações sobre o estado da sessão entre as requisições.
     *
     * @param http Configuração do HttpSecurity para personalizar a segurança HTTP.
     * @return A configuração do filtro de segurança, incluindo regras de autorização e gestão de sessão.
     * @throws Exception Se ocorrer um erro durante a configuração do filtro de segurança.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "api/v1/usuarios").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/v1/auth").permitAll()
                        .anyRequest().authenticated()
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(
                        jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class
                ).build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    /**
     * Retorna um codificador de senha (PasswordEncoder) usando o algoritmo BCrypt.
     * <p>
     * O BCrypt é um algoritmo de hash seguro e amplamente utilizado para armazenar senhas de forma criptografada.
     * Ele aplica uma função de hash e um sal (salt) para gerar o hash da senha, dificultando ataques de força bruta.
     *
     * @return O codificador de senha baseado no algoritmo BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Retorna o gerenciador de autenticação (AuthenticationManager) usado pelo Spring Security para autenticar os usuários.
     * <p>
     * O AuthenticationManager é um componente essencial para o Spring Security,
     * responsável por verificar as credenciais do usuário e determinar se ele está autorizado a acessar recursos protegidos.
     *
     * @param authenticationConfiguration Configuração de autenticação do Spring Security.
     * @return O AuthenticationManager configurado.
     * @throws Exception Se ocorrer um erro ao obter o AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
