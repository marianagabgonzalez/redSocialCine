package com.egg.cinefilos.servicios;

import com.egg.cinefilos.entidades.*;
import com.egg.cinefilos.excepciones.ErrorServicio;
import com.egg.cinefilos.repositorios.RepUsuario;
import com.egg.cinefilos.repositorios.RepoComentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioServicio {
    @Autowired
    RepUsuario repUsuario;

    @Autowired
    PeliculaServicio peliculaServicio;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RepoComentario repoComentario;

    @Autowired
    FotoServicio fotoServicio;

    public void validar(String username, String contrasenia, String contrasenia2) throws ErrorServicio {
        if(username.isEmpty()) {
            throw new ErrorServicio("El usuario no puede estar vacío");
        }

        if(contrasenia.isEmpty() || contrasenia.length() < 6) {
            throw new ErrorServicio("La contraseña es demasiado corta");
        }

        if(!contrasenia.equals(contrasenia2)) {
            throw new ErrorServicio("Las contraseñas no coinciden");
        }

        List<Usuario> todosLosUsuarios = (ArrayList)repUsuario.findAll();

        for(Usuario u: todosLosUsuarios) {
            if(username.equals(u.getUsername())) {
                throw new ErrorServicio("Nombre de usuario no disponible");
            }
        }
    }

    @Transactional
    public Usuario guardarUsuario(String username, String contrasenia, String contrasenia2, MultipartFile archivo) throws ErrorServicio{
        validar(username, contrasenia, contrasenia2);
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setContrasenia(passwordEncoder.encode(contrasenia));
        usuario.setRol(Role.USER);
        usuario.setPuntaje(0d);
        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);
        return repUsuario.save(usuario);
    }

    @Transactional
    public Usuario modificarFoto (Usuario usuario, MultipartFile archivo) {
        String idFoto = null;
        if (usuario.getFoto()!=null) {
            idFoto = usuario.getFoto().getId();
        }
        Foto foto = fotoServicio.actualizar(idFoto, archivo);
        usuario.setFoto(foto);

        return repUsuario.save(usuario);
    }

    @Transactional
    public Usuario agregarListaFavoritas(Usuario usuario, String titulo) throws ErrorServicio{
        Optional<Usuario> respuesta = repUsuario.findById(usuario.getId());
        if(respuesta.isPresent()) {
            Usuario u = respuesta.get();
            u.setUsername(usuario.getUsername());
            u.setContrasenia(usuario.getContrasenia());
            u.setId(usuario.getId());
            u.setPeliculasFavoritas(usuario.getPeliculasFavoritas());
            u.setPeliculasPorVer(usuario.getPeliculasPorVer());

            Pelicula peliculaNueva = peliculaServicio.buscarPorTitulo(titulo);
            Set<Pelicula> peliculasDelUsuario = u.getPeliculasFavoritas();
            peliculasDelUsuario.add(peliculaNueva);

            u.setPeliculasFavoritas(peliculasDelUsuario);
            return repUsuario.save(u);
        } else {
            throw new ErrorServicio("Usuario no encontrado");
        }
    }

    public Usuario agregarListaPorVer(Usuario usuario, String titulo) throws ErrorServicio{
        Optional<Usuario> respuesta = repUsuario.findById(usuario.getId());
        if(respuesta.isPresent()) {
            Usuario u = respuesta.get();
            u.setUsername(usuario.getUsername());
            u.setContrasenia(usuario.getContrasenia());
            u.setId(usuario.getId());
            u.setPeliculasFavoritas(usuario.getPeliculasFavoritas());
            u.setPeliculasPorVer(usuario.getPeliculasPorVer());

            Pelicula peliculaNueva = peliculaServicio.buscarPorTitulo(titulo);
            Set<Pelicula> peliculasDelUsuario = u.getPeliculasPorVer();
            peliculasDelUsuario.add(peliculaNueva);

            u.setPeliculasPorVer(peliculasDelUsuario);
            return repUsuario.save(u);
        } else {
            throw new ErrorServicio("Usuario no encontrado");
        }
    }

    public Usuario seguirUsuario(Usuario usuario, String username) throws ErrorServicio{
        Optional<Usuario> respuesta = repUsuario.findById(usuario.getId());
        if(respuesta.isPresent()) {
            Usuario u = respuesta.get();
            u.setUsername(usuario.getUsername());
            u.setContrasenia(usuario.getContrasenia());
            u.setId(usuario.getId());
            u.setPeliculasFavoritas(usuario.getPeliculasFavoritas());
            u.setPeliculasPorVer(usuario.getPeliculasPorVer());
            u.setSeguidos(usuario.getSeguidos());

            Usuario seguido = repUsuario.findByUsername(username).get();

            Set<Usuario> seguidosDelUsuario = u.getSeguidos();
            seguidosDelUsuario.add(seguido);

            if(!u.getUsername().equals(seguido.getUsername())) {
                u.setSeguidos(seguidosDelUsuario);
            } else {
                throw new ErrorServicio("No puedes seguirte a ti mismo");
            }

            return repUsuario.save(u);
        } else {
            throw new ErrorServicio("Usuario no encontrado");
        }
    }

    public Iterable<Usuario> listarUsuarios() {
        return repUsuario.findAll();
    }

    public List<Usuario> buscarPorPalabraClave(String palabra){
        return repUsuario.findByUsernameContaining(palabra);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repUsuario.findById(id);
    }

    @Transactional
    public boolean borrarUsuario(Long id) throws ErrorServicio{
        Optional<Usuario> respuesta = repUsuario.findById(id);

        if(respuesta.isPresent()) {
            Usuario u = respuesta.get();
            repUsuario.deleteById(u.getId());
            return true;
        } else {
            throw new ErrorServicio("Usuario no encontrado");
        }
    }

    public void calcularPuntaje(Usuario usuario) {
        Double suma = 0d;
        for(Comentario c : repoComentario.findByUsuarioId(usuario.getId())) {
            suma+= c.getValoracion().getPromedio();
        }
        Double cantidadComentarios = 0d;

        for (Comentario c:
                repoComentario.findByUsuarioId(usuario.getId())) {
            if(c.getValoracion().getPromedio()> 0) {
                cantidadComentarios++;
            }
        }

        Double puntaje = suma/cantidadComentarios;
        usuario.setPuntaje(puntaje);
        repUsuario.save(usuario);
    }

    public Long devolverUltimoComentario(Usuario usuario) {
        List<Comentario> comentarios = repoComentario.findByUsuarioId(usuario.getId());
        List<Long> ids = new ArrayList<>();

        for (Comentario c:
             comentarios) {
            ids.add(c.getId());
        }

        try {
            return Collections.max(ids);
        } catch (NoSuchElementException e) {
            return null;
        }

    }

    public List<Comentario> devolverUltimoComentarioSeguidos(Usuario usuario) {
        List<Comentario> ultimosComentarios = new ArrayList<>();

        for (Usuario u:
             usuario.getSeguidos()) {
            Long idC = devolverUltimoComentario(u);
            if(idC != null) {
                ultimosComentarios.add(repoComentario.findById(idC).get());
            }
        }
        return ultimosComentarios;
    }

    public Usuario dejarDeSeguir(Usuario usuario, String username) throws ErrorServicio{
        Optional<Usuario> respuesta = repUsuario.findById(usuario.getId());

        if(respuesta.isPresent()) {
            Usuario u1 = respuesta.get();
            Optional<Usuario> resp = repUsuario.findByUsername(username);
            if(resp.isPresent()) {
                Usuario u2 = resp.get();
                u1.getSeguidos().remove(u2);
                return repUsuario.save(u1);
            } else {
                throw new ErrorServicio("Usted no sigue al usuario seleccionado");
            }
        } else {
            throw new ErrorServicio("Usuario no encontrado");
        }
    }

    public Usuario eliminarFavorita(Usuario usuario, Long id) throws ErrorServicio{

        Optional<Usuario> respuesta = repUsuario.findById(usuario.getId());
        if(respuesta.isPresent()) {
            Usuario u = respuesta.get();

            Pelicula peliculaEliminar = peliculaServicio.buscarPorId(id).get();
            u.getPeliculasFavoritas().remove(peliculaEliminar);

            return repUsuario.save(u);
        } else {
            throw new ErrorServicio("Usuario no encontrado");
        }

    }

    public Usuario eliminarPorVer(Usuario usuario, Long id) throws ErrorServicio{

        Optional<Usuario> respuesta = repUsuario.findById(usuario.getId());
        if(respuesta.isPresent()) {
            Usuario u = respuesta.get();

            Pelicula peliculaEliminar = peliculaServicio.buscarPorId(id).get();
            u.getPeliculasPorVer().remove(peliculaEliminar);

            return repUsuario.save(u);
        } else {
            throw new ErrorServicio("Usuario no encontrado");
        }

    }
}
