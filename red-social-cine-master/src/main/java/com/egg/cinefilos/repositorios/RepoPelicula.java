package com.egg.cinefilos.repositorios;

import com.egg.cinefilos.entidades.Pelicula;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoPelicula extends CrudRepository<Pelicula, Long> {
    Pelicula findByTitulo(String titulo);
    List<Pelicula> findByTituloContaining(String titulo);
    List<Pelicula> findByDirectorContaining(String director);
    List<Pelicula> findByGeneroContaining(String genero);
    List<Pelicula> findTop4ByOrderByIdDesc();
    List<Pelicula> findByOrderByTituloAsc();
    List<Pelicula> findByOrderByDirectorAsc();
    List<Pelicula> findByActoresContaining(String actor);
}
