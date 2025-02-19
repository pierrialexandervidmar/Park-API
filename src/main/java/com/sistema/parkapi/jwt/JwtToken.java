package com.sistema.parkapi.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um token JWT usado para autenticação.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtToken {

    /**
     * O token JWT.
     */
    private String token;
}
