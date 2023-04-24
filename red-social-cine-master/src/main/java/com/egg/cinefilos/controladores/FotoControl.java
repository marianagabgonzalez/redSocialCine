package com.egg.cinefilos.controladores;

import com.egg.cinefilos.entidades.Pelicula;
import com.egg.cinefilos.entidades.Usuario;
import com.egg.cinefilos.excepciones.ErrorServicio;
import com.egg.cinefilos.repositorios.RepUsuario;
import com.egg.cinefilos.servicios.FotoServicio;
import com.egg.cinefilos.servicios.PeliculaServicio;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.egg.cinefilos.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/foto")
public class FotoControl {

    @Autowired
    private FotoServicio fotosv;

    @Autowired
    private PeliculaServicio pelisv;

    @Autowired
    private RepUsuario repUsuario;

    @GetMapping("/peliculas/{id}")
    public ResponseEntity<byte[]> fotoPeli(@PathVariable Long id) {
        try {
            Pelicula pelicula = pelisv.buscarPorId(id).get();
            if (pelicula.getFoto()==null) {
                throw new ErrorServicio ("La pelicula no tiene foto");
            }

            byte [] foto = pelicula.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto,headers,HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(FotoControl.class.getName()).log(Level.SEVERE,null,HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/usuario/{username}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String username) {
        try {
            Usuario usuario = repUsuario.findByUsername(username).get();
            if (usuario.getFoto()==null) {
                throw new ErrorServicio ("La pelicula no tiene foto");
            }

            byte [] foto = usuario.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto,headers,HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(FotoControl.class.getName()).log(Level.SEVERE,null,HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
