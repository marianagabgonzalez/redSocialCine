package com.egg.cinefilos.servicios;

import com.egg.cinefilos.entidades.*;
import com.egg.cinefilos.excepciones.ErrorServicio;
import com.egg.cinefilos.repositorios.RepoComentario;
import com.egg.cinefilos.repositorios.RepoPelicula;
import com.egg.cinefilos.repositorios.RepoValoracion;
import com.egg.cinefilos.repositorios.RepoValoracionComentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ValoracionComentarioServicio {
    @Autowired
    RepoValoracionComentario repoValoracionComentario;

    @Autowired
    RepoComentario repoComentario;

    @Autowired
    UsuarioServicio usuarioServicio;

    public ValoracionComentario guardarValoracion(ValoracionComentario valoracion, Long idComentario) {
        valoracion.setComentario(repoComentario.findById(idComentario).get());
        return repoValoracionComentario.save(valoracion);
    }

    public void borrarValoracion(ValoracionComentario valoracion){
        repoValoracionComentario.delete(valoracion);
    }

    @Transactional
    public ValoracionComentario valorar(ValoracionComentario valoracion, Long idComentario) throws ErrorServicio {
        if(valoracion.getUltima() > 5 || valoracion.getUltima() < 1) {
            throw new ErrorServicio("Valoración no válida");
        }

        Optional<Comentario> respuesta = repoComentario.findById(idComentario);

        if (respuesta.isPresent()) {
            Comentario c1 = respuesta.get();
            ValoracionComentario v1 = repoValoracionComentario.findByComentarioId(idComentario);

            v1.setCantidad(v1.getCantidad()+1);
            v1.setTotal(v1.getTotal()+valoracion.getUltima());
            v1.setPromedio(v1.getTotal()/v1.getCantidad());

            usuarioServicio.calcularPuntaje(c1.getUsuario());

            return repoValoracionComentario.save(v1);
        } else {
            throw new ErrorServicio("Pelicula no encontrada");
        }
    }
}
