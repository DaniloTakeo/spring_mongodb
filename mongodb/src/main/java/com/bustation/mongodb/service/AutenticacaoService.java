package com.bustation.mongodb.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bustation.mongodb.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {

	private final UsuarioRepository repository;
	
	@Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        return repository.findByLogin(username)
        		.map(usuario -> {
                    return usuario;
                })
                .orElseThrow(() -> {
                    return new UsernameNotFoundException(
                            "Usuário não encontrado");
                });
    }
}
