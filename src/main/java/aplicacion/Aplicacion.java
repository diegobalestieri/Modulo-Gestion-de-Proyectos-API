package aplicacion;

import controladores.ProyectoController;
import modelo.Proyecto;
import persistencia.EntidadProyecto;
import persistencia.ProyectosRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import servicio.ProyectoService;

@SpringBootApplication
@ComponentScan(basePackageClasses = {ProyectoController.class, ProyectoService.class, ProyectosRepository.class})
@EntityScan(basePackageClasses = EntidadProyecto.class)
@EnableJpaRepositories(basePackageClasses = ProyectosRepository.class)
public class Aplicacion {

	public static void main(String[] args) {
		SpringApplication.run(Aplicacion.class, args);
	}

}
