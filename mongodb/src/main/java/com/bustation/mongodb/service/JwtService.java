package com.bustation.mongodb.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 */
@Service
public class JwtService {

    /**
     * Segredo para assinatura dos tokens JWT, configurado no application.yml.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Tempo de expiração do token JWT em milissegundos.
     */
    @Value("${jwt.expiration}")
    private Long expirationMillis;

    /**
     * Retorna a chave usada para assinar os tokens.
     *
     * @return a chave de assinatura
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Gera um token JWT para o usuário fornecido.
     *
     * @param usuario o usuário autenticado
     * @return o token JWT assinado
     */
    public String gerarToken(final Usuario usuario) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expirationMillis);

        return Jwts.builder()
            .setSubject(usuario.getLogin())
            .setIssuedAt(agora)
            .setExpiration(expiracao)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Valida um token JWT e retorna o login do usuário contido no token.
     *
     * @param token o token JWT
     * @return o login do usuário
     */
    public String validarToken(final String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }
}
