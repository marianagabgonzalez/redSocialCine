package com.egg.cinefilos.controladores;

import com.egg.cinefilos.entidades.ValoracionComentario;
import com.egg.cinefilos.excepciones.ErrorServicio;
import com.egg.cinefilos.servicios.ValoracionComentarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ValoracionComentarioControl {
    @Autowired
    ValoracionComentarioServicio valoracionComentarioServicio;

    @PostMapping("/pelicula/detalles/{idP}/valorar/{idC}")
    public String valorarPelicula(@PathVariable Long idP, @PathVariable Long idC, ValoracionComentario valoracion) {
        try {
            valoracionComentarioServicio.valorar(valoracion, idC);
            return "redirect:/pelicula/detalles/{idP}";
        } catch (ErrorServicio e) {
            return "redirect:/error";
        }
    }
}