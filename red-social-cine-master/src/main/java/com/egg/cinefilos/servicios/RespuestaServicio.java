package com.egg.cinefilos.servicios;

import com.egg.cinefilos.entidades.Respuesta;
import com.egg.cinefilos.excepciones.ErrorServicio;
import com.egg.cinefilos.repositorios.RepoRespuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RespuestaServicio {
    @Autowired
    RepoRespuesta repoRespuesta;

    @Transactional
    public void publicarRespuesta(Respuesta respuesta) throws ErrorServicio {
        if(respuesta.getTexto().isEmpty()) {
            throw new ErrorServicio("La respuesta no puede estar vacía");
        } else {
            repoRespuesta.save(respuesta);
        }
    }

    public Optional<Respuesta> buscarPorId(Long id) {
        return repoRespuesta.findById(id);
    }

    public List<Respuesta> buscarPorComentario(Long cId) {
        return repoRespuesta.findByComentarioId(cId);
    }

    @Transactional
    public boolean borrarRespuesta(Long id) {
        try {
            repoRespuesta.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("Respuesta no enocntrada");
            return false;
        }
    }
}
