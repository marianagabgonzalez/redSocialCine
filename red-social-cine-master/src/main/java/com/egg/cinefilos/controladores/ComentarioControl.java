package com.egg.cinefilos.controladores;

import com.egg.cinefilos.entidades.*;
import com.egg.cinefilos.excepciones.ErrorServicio;
import com.egg.cinefilos.repositorios.RepUsuario;
import com.egg.cinefilos.servicios.ComentarioServicio;
import com.egg.cinefilos.servicios.PeliculaServicio;
import com.egg.cinefilos.servicios.RespuestaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ComentarioControl {
    @Autowired
    ComentarioServicio comenSV;

    @Autowired
    PeliculaServicio peliculaSV;

    @Autowired
    RepUsuario repUsuario;

    @Autowired
    RespuestaServicio respuestaServicio;

    @PostMapping("pelicula/detalles/{idP}/nuevo")
    public String nuevoComentario(@PathVariable Long idP, @ModelAttribute("comentario") Comentario comentario, Authentication auth, ModelMap model) throws ErrorServicio {
        Usuario usuario = repUsuario.findByUsername(auth.getName()).orElse(null);
        comentario.setPelicula(peliculaSV.buscarPorId(idP).get());
        comentario.setUsuario(usuario);
        try {
            comenSV.crearComentario(comentario);
            return "redirect:/pelicula/detalles/{idP}";
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("pelicula/detalles/{idP}/comentario/{id}")
    public String respuestasDelComentario(@PathVariable Long idP, @PathVariable Long id, Model model) {
        model.addAttribute("comentario", comenSV.buscarPorId(id).get());
        model.addAttribute("respuestas", respuestaServicio.buscarPorComentario(id));
        model.addAttribute("pelicula", peliculaSV.buscarPorId(idP).get());
        model.addAttribute("valoracion", new ValoracionComentario());
        Respuesta nuevaRespuesta = new Respuesta();
        model.addAttribute("nuevaRespuesta", nuevaRespuesta);
        return "respuestas_comentario";
    }
}
