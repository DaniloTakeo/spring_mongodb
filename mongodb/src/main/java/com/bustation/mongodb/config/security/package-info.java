/**
 * Este pacote contém as classes responsáveis pela
 * configuração de segurança da aplicação.
 *
 * <p>Inclui a definição da cadeia de filtros de segurança
 * {@link org.springframework.security.web.SecurityFilterChain},
 * configurações de autenticação com JWT,
 * gerenciamento de sessão sem estado (stateless)
 * e definição de beans como
 * {@link org.springframework.security.authentication.AuthenticationManager}
 * e {@link org.springframework.security.crypto.password.PasswordEncoder}.
 *
 * <p>Utiliza o Spring Security com autenticação
 * baseada em token JWT, fornecendo proteção
 * para os recursos da aplicação e suporte à
 * documentação com Swagger, monitoramento com Actuator,
 * e endpoints públicos para login e registro.
 *
 * @see com.bustation.mongodb.config.security.SecurityConfig
 * @see com.bustation.mongodb.config.security.JwtFilter
 */
package com.bustation.mongodb.config.security;
