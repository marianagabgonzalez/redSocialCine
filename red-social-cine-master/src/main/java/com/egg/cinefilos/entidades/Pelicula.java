package com.egg.cinefilos.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String director;
    private String actores;
    private String sinopsis;
    private String extracto;
    private Integer duracion;
    private String genero;
    private Integer anio;


    @OneToOne
    private Foto foto;

    public Pelicula(String titulo, String director, String actores, Integer duracion, String genero, Integer anio) {
        this.titulo = titulo;
        this.director = director;
        this.actores = actores;
        this.duracion = duracion;
        this.genero = genero;
        this.anio = anio;
    }

    public Pelicula(String titulo, String director, String actores, String sinopsis, Integer duracion, String genero, Integer anio, Foto foto) {
        this.titulo = titulo;
        this.director = director;
        this.actores = actores;
        this.sinopsis = sinopsis;
        this.duracion = duracion;
        this.genero = genero;
        this.anio = anio;
        this.foto = foto;
    }
}
