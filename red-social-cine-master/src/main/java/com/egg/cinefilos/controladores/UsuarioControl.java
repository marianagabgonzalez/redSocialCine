package com.egg.cinefilos.controladores;

import com.egg.cinefilos.entidades.Foto;
import com.egg.cinefilos.entidades.Pelicula;
import com.egg.cinefilos.entidades.Usuario;
import com.egg.cinefilos.excepciones.ErrorServicio;
import com.egg.cinefilos.repositorios.RepUsuario;
import com.egg.cinefilos.repositorios.RepoComentario;
import com.egg.cinefilos.servicios.FotoServicio;
import com.egg.cinefilos.servicios.PeliculaServicio;
import com.egg.cinefilos.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UsuarioControl {
    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    RepUsuario repUsuario;

    @Autowired
    PeliculaServicio peliculaServicio;

    @Autowired
    RepoComentario repoComentario;

    @Autowired
    FotoServicio fotoServicio;

    @GetMapping("/registrar")
    public String nuevoUsuario(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);

        return "nuevo_usuario";
    }


    @PostMapping("/registrar/nuevo")
    public String registroDeUsuario(@ModelAttribute("usuario") @RequestParam String username, @RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam MultipartFile archivo, ModelMap model) throws ErrorServicio{
        try {
            usuarioServicio.guardarUsuario(username, contrasenia, contrasenia2, archivo);
            return "redirect:/iniciar";
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            return "nuevo_usuario";
        }

    }

    @GetMapping("/iniciar")
    public String iniciarSesion(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);

        return "iniciar_sesion";
    }

    @GetMapping ("/usuario/modificar/{username}")
    public String modificarfotoForm (@PathVariable String username, Model model, Authentication auth) {
        if(auth.getName().equals(username)) {
            model.addAttribute("usuario", repUsuario.findByUsername(username).get());
            return "modificarfoto_usuario";
        } else {
            return "sin_permiso";
        }
    }

    @PostMapping("/usuario/modificarfoto/{username}")
    public String editarFoto(@PathVariable String username, MultipartFile archivo) {
        Usuario usuario = repUsuario.findByUsername(username).get();
        usuarioServicio.modificarFoto(usuario, archivo);
        return "redirect:/usuario/{username}";
    }

    @GetMapping("/usuario/{username}")
    public String paginaUsuario(@PathVariable String username, Model model, Authentication auth) {
        Usuario usuario = repUsuario.findByUsername(auth.getName()).get();
        model.addAttribute("seguidos", usuario.getSeguidos());
        model.addAttribute("usuarioAMostrar", repUsuario.findByUsername(username).get());
        model.addAttribute("comentarios", repoComentario.findByUsuarioId(repUsuario.findByUsername(username).get().getId()));
        return "pagina_usuario";
    }

    @PostMapping("/pelicula/favorita/{id}")
    public String agregarAFavorita(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuario, Authentication auth) {
        usuario = repUsuario.findByUsername(auth.getName()).get();
        Pelicula favorita = peliculaServicio.buscarPorId(id).get();
        try {
            usuarioServicio.agregarListaFavoritas(usuario, favorita.getTitulo());
            return "redirect:/pelicula/detalles/{id}";
        } catch (ErrorServicio e) {
            return null;
        }
    }

    @PostMapping("/pelicula/por_ver/{id}")
    public String agregarAPorVer(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuario, Authentication auth) {
        usuario = repUsuario.findByUsername(auth.getName()).get();
        Pelicula favorita = peliculaServicio.buscarPorId(id).get();
        try {
            usuarioServicio.agregarListaPorVer(usuario, favorita.getTitulo());
            return "redirect:/pelicula/detalles/{id}";
        } catch (ErrorServicio e) {
            return null;
        }
    }

    @PostMapping("/pelicula/favorita/{id}/eliminar")
    public String eliminarFavorita(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuario, Authentication auth) {
       usuario = repUsuario.findByUsername(auth.getName()).get();

        try {
            usuarioServicio.eliminarFavorita(usuario, id);
            return "redirect:/pelicula/detalles/{id}";
        } catch (ErrorServicio e) {
            return null;
        }
    }

    @PostMapping("/pelicula/por_ver/{id}/eliminar")
    public String eliminarPorVer(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuario, Authentication auth) {
        usuario = repUsuario.findByUsername(auth.getName()).get();

        try {
            usuarioServicio.eliminarPorVer(usuario, id);
            return "redirect:/pelicula/detalles/{id}";
        } catch (ErrorServicio e) {
            return null;
        }
    }

    @PostMapping("/usuario/{username}/seguir")
    public String seguirUsuario(@PathVariable String username, @ModelAttribute("usuario") Usuario usuario, Authentication auth) {
        usuario = repUsuario.findByUsername(auth.getName()).get();
        try {
            usuarioServicio.seguirUsuario(usuario, username);
            return "redirect:/usuario/{username}";
        } catch (ErrorServicio e) {
            return null;
        }
    }

    @PostMapping("/usuario/{username}/no_seguir")
    public String dejarDeSeguir(@PathVariable String username, @ModelAttribute("usuario") Usuario usuario, Authentication auth) {
        usuario = repUsuario.findByUsername(auth.getName()).get();

        try {
            usuarioServicio.dejarDeSeguir(usuario, username);
            return "redirect:/usuario/{username}";
        } catch (ErrorServicio e) {
            return null;
        }
    }

}
