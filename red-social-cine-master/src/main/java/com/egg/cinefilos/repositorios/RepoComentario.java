package com.egg.cinefilos.repositorios;

import com.egg.cinefilos.entidades.Comentario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoComentario extends CrudRepository<Comentario, Long> {
    List<Comentario> findByPeliculaId(Long id);
    List<Comentario> findByUsuarioId(Long id);
}
