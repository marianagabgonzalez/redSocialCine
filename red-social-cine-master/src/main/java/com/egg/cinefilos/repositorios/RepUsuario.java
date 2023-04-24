package com.egg.cinefilos.repositorios;

import com.egg.cinefilos.entidades.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepUsuario extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String inputName);
    List<Usuario> findByUsernameContaining(String username);
    List<Usuario> findTop10ByOrderByPuntajeDesc();
}
