package com.egg.cinefilos.controladores;

import com.egg.cinefilos.entidades.Valoracion;
import com.egg.cinefilos.excepciones.ErrorServicio;
import com.egg.cinefilos.servicios.ValoracionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ValoracionControl {
    @Autowired
    ValoracionServicio valoracionServicio;
   // @Autowired
   // RepUsuario repUsuario;
   // @Autowired
   // RepoPelicula repopeli;

    @PostMapping("/pelicula/detalles/{idP}/valorar")
    public String valorarPelicula(@PathVariable Long idP, Valoracion valoracion) {
        try {
            valoracionServicio.valorar(valoracion, idP);
            return "redirect:/pelicula/detalles/{idP}";
        } catch (ErrorServicio e) {
            return "redirect:/error";
        }
    }
    
   /* @PostMapping("/pelicula/detalles/{idP}/valorar")
    public String valorarPelicula(@PathVariable Long idP, Valoracion valoracion, Authentication auth) {
        Usuario usuario = repUsuario.findByUsername(auth.getName()).get();
        
        if  (respuesta.get
        try {
            valoracionServicio.valorar(valoracion, idP,usuario);
            return "redirect:/pelicula/detalles/{idP}";
        } catch (ErrorServicio e) {
            return "redirect:/error";
        }
    }*/
}
