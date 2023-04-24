package com.egg.cinefilos;

import com.egg.cinefilos.entidades.Foto;
import com.egg.cinefilos.entidades.Role;
import com.egg.cinefilos.entidades.Usuario;
import com.egg.cinefilos.repositorios.RepFoto;
import com.egg.cinefilos.repositorios.RepUsuario;
import com.egg.cinefilos.repositorios.RepoPelicula;
import com.egg.cinefilos.repositorios.RepoValoracion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CinefilosApplication {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RepoValoracion repoValoracion;

	@Autowired
	RepoPelicula repoPelicula;

	@Autowired
	RepFoto repFoto;

	public static void main(String[] args) {
		SpringApplication.run(CinefilosApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(RepUsuario repUsuario) {
		return (args) -> {
			Usuario usuario = new Usuario();
			usuario.setContrasenia(passwordEncoder.encode("admin"));
			usuario.setUsername("admin4");
			usuario.setRol(Role.ADMIN);
			Foto foto = new Foto();
			//repFoto.save(foto);
			//usuario.setFoto(foto);
			//repUsuario.save(usuario);
		};
	}
}
