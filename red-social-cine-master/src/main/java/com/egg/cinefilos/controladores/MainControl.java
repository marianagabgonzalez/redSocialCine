package com.egg.cinefilos.controladores;

import com.egg.cinefilos.entidades.Usuario;
import com.egg.cinefilos.repositorios.RepUsuario;
import com.egg.cinefilos.repositorios.RepoPelicula;
import com.egg.cinefilos.repositorios.RepoValoracion;
import com.egg.cinefilos.repositorios.RepoValoracionComentario;
import com.egg.cinefilos.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainControl {
    @Autowired
    RepoPelicula repoPelicula;

    @Autowired
    RepoValoracion repoValoracion;

    @Autowired
    RepUsuario repUsuario;

    @Autowired
    RepoValoracionComentario repoValoracionComentario;

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("peliculasNuevas", repoPelicula.findTop4ByOrderByIdDesc());
        model.addAttribute("mejorValoradas", repoValoracion.findTop4ByOrderByPromedioDesc());
        model.addAttribute("topResenias", repoValoracionComentario.findTop9ByOrderByPromedioDesc());
        model.addAttribute("topUsuarios", repUsuario.findTop10ByOrderByPuntajeDesc());
        return "index";
    }

    @GetMapping("/personal")
    public String miFeed(Model model, Authentication auth) {
        Usuario usuario = repUsuario.findByUsername(auth.getName()).orElse(null);
        model.addAttribute("seguidos", usuarioServicio.devolverUltimoComentarioSeguidos(usuario));
        return "feed_personal";
    }

    /*
    @GetMapping("/error")
    public String error() {
        return "error";
    }

     */
}
