package com.egg.cinefilos.repositorios;


import com.egg.cinefilos.entidades.Respuesta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoRespuesta extends CrudRepository<Respuesta, Long> {
    List<Respuesta> findByComentarioId(Long id);
}
