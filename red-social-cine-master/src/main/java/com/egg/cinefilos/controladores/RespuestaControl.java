package com.egg.cinefilos.controladores;

import com.egg.cinefilos.entidades.Respuesta;
import com.egg.cinefilos.entidades.Usuario;
import com.egg.cinefilos.excepciones.ErrorServicio;
import com.egg.cinefilos.repositorios.RepUsuario;
import com.egg.cinefilos.servicios.ComentarioServicio;
import com.egg.cinefilos.servicios.RespuestaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RespuestaControl {
    @Autowired
    RespuestaServicio respuestaServicio;

    @Autowired
    RepUsuario repUsuario;

    @Autowired
    ComentarioServicio comentarioServicio;


    @PostMapping("/pelicula/detalles/{idP}/comentario/{idC}/nueva_respuesta")
    public String nuevaRespuesta(@PathVariable Long idP, @PathVariable Long idC, @ModelAttribute("respuesta")Respuesta respuesta, Authentication auth, ModelMap model) {
        Usuario usuario = repUsuario.findByUsername(auth.getName()).orElse(null);
        respuesta.setComentario(comentarioServicio.buscarPorId(idC).get());
        respuesta.setUsuario(usuario);
        try {
            respuestaServicio.publicarRespuesta(respuesta);
            return "redirect:/pelicula/detalles/{idP}/comentario/{idC}";
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            return "error";
        }
    }
}
