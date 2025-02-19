package com.sistema.parkapi.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Classe utilitária para manipulação de tokens JWT (JSON Web Tokens).
 *
 * Esta classe fornece métodos para criar tokens JWT, extrair claims e validar tokens.
 * Utiliza a biblioteca {@code io.jsonwebtoken} para o processamento dos JWTs e inclui uma
 * chave secreta para assinar os tokens.
 *
 */
@Slf4j
public class JwtUtils {

    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "3f8a7b9d6e4c1e9d2c9a8f6e2b3d7c6a4f5a9b3d2e7f1c6";
    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 2;

    /**
     * Construtor privado para evitar a instanciação da classe.
     */
    private JwtUtils() {
    }

    /**
     * Gera uma chave secreta usada para assinar os tokens JWT.
     *
     * @return Um objeto {@link Key} representando a chave secreta.
     */
    private static Key generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Calcula a data de expiração do token com base na data de início e nos valores
     * de expiração configurados (dias, horas e minutos).
     *
     * @param start A data de início a partir da qual a expiração será calculada.
     * @return Um objeto {@link Date} representando a data de expiração.
     */
    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Cria um token JWT para o usuário informado com uma determinada role (papel).
     *
     * @param username Nome do usuário para o qual o token será gerado.
     * @param role Papel do usuário que será incluído no token.
     * @return Um objeto {@link JwtToken} contendo o token gerado.
     */
    public static JwtToken createToken(String username, String role) {
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(limit)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .claim("role", role)
                .compact();

        return new JwtToken(token);
    }

    /**
     * Extrai os claims (informações) de um token JWT.
     *
     * @param token O token JWT a ser analisado.
     * @return Um objeto {@link Claims} contendo os dados do token, ou {@code null} se o token for inválido.
     */
    public static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();
        } catch (JwtException ex) {
            log.error(String.format("Token inválido: %s", ex.getMessage()));
        }
        return null;
    }

    /**
     * Recupera o nome do usuário (subject) a partir de um token JWT.
     *
     * @param token O token JWT a ser analisado.
     * @return O nome do usuário contido no token.
     */
    public static String getUsennameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Verifica se um token JWT é válido.
     *
     * @param token O token JWT a ser validado.
     * @return {@code true} se o token for válido, caso contrário {@code false}.
     */
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token));
            return true;
        } catch (JwtException ex) {
            log.error(String.format("Token inválido: %s", ex.getMessage()));
        }
        return false;
    }

    /**
     * Remove o prefixo "Bearer " do token, se ele estiver presente.
     *
     * @param token O token JWT com ou sem o prefixo.
     * @return O token sem o prefixo "Bearer ".
     */
    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }
}