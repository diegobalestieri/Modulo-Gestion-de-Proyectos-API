package TestsProyecto;

import aplicacion.Aplicacion;
import controladores.ProyectoController;
import modelo.Estado.EstadoProyecto;
import modelo.Proyecto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import persistencia.ProyectosRepository;
import servicio.ProyectoService;

@Configuration
@ComponentScan(basePackageClasses = {
        Proyecto.class,
        ProyectoController.class,
        Aplicacion.class,
        EstadoProyecto.class,
        ProyectoService.class,
        ProyectosRepository.class})
@PropertySource("classpath:application.properties")
public class ConfiguradorContextoCucumber {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();

    }
}