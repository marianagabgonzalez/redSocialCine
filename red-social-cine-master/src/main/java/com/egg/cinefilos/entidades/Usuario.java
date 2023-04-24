package com.egg.cinefilos.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String contrasenia;

    @Transient
    private String contrasenia2;

    private Double puntaje;

    @Enumerated(EnumType.STRING)
    private Role rol;

    @ManyToMany
    private Set<Pelicula> peliculasFavoritas;

    @ManyToMany
    private Set<Pelicula> peliculasPorVer;

    @ManyToMany
    private Set<Usuario> seguidos;

    @OneToOne
    private Foto foto;

    public Usuario(String usarname, String contrasenia) {
        this.username = usarname;
        this.contrasenia = contrasenia;
    }
}
