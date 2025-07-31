/**
 * Contém os controladores REST responsáveis por expor os endpoints da API
 * Rodoviária. Cada classe neste pacote lida com as requisições HTTP e delega
 * as operações principais para os serviços apropriados. O foco é intermediar
 * a comunicação entre os clientes da API e a lógica de negócios da aplicação.
 *
 * Os controladores são anotados com
 * {@link org.springframework.web.bind.annotation.
 * RestController} e usam anotações adicionais do Spring e Swagger para
 * documentação, como
 * {@link org.springframework.web.bind.annotation.RequestMapping},
 * {@link io.swagger.v3.oas.annotations.Operation} e
 * {@link io.swagger.v3.oas.annotations.tags.Tag}.
 */
package com.bustation.mongodb.controller;
