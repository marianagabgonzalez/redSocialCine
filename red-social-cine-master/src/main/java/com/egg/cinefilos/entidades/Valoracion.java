package com.egg.cinefilos.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double cantidad;
    private Double total;
    private Double promedio;
    private Double ultima;
    //private HashSet<Long> chequeoUsuarios;

    @OneToOne
    private Pelicula pelicula;

    public Valoracion(Double cantidad, Double total, Double promedio, Double ultima, Pelicula pelicula) {
        this.cantidad = cantidad;
        this.total = total;
        this.promedio = promedio;
        this.ultima = ultima;
        this.pelicula = pelicula;
    }
}
