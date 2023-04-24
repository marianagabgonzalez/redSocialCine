package com.egg.cinefilos.seguridad;

import com.egg.cinefilos.entidades.Usuario;
import com.egg.cinefilos.repositorios.RepUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    RepUsuario repUsuario;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputName -> {
            /*If client is not found on repository throws and exception using .orElseThrow with Supplier */
            Usuario usuario = repUsuario.findByUsername(inputName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "User not found"));

            //Ordinal 0 for ENUM UserRol = ADMIN
            if (usuario.getRol().ordinal() == 0) {
                return new User(usuario.getUsername(), usuario.getContrasenia(), AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
            }

            return new User(usuario.getUsername(), usuario.getContrasenia(), AuthorityUtils.createAuthorityList("USER"));
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
