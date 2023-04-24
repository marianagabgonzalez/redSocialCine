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
public class ValoracionComentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double cantidad;
    private Double total;
    private Double promedio;
    private Double ultima;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_id")
    private Comentario comentario;

    public ValoracionComentario(Double cantidad, Double total, Double promedio, Double ultima, Comentario comentario) {
        this.cantidad = cantidad;
        this.total = total;
        this.promedio = promedio;
        this.ultima = ultima;
        this.comentario = comentario;
    }
}
