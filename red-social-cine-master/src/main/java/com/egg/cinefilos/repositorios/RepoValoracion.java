package com.egg.cinefilos.repositorios;

import com.egg.cinefilos.entidades.Valoracion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepoValoracion extends CrudRepository<Valoracion, Long> {
    Valoracion findByPeliculaId(Long id);
    List<Valoracion> findTop4ByOrderByPromedioDesc();
}
