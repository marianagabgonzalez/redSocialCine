package com.egg.cinefilos.repositorios;


import com.egg.cinefilos.entidades.ValoracionComentario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepoValoracionComentario extends CrudRepository<ValoracionComentario, Long> {
    ValoracionComentario findByComentarioId(Long id);
    List<ValoracionComentario> findTop9ByOrderByPromedioDesc();
}
