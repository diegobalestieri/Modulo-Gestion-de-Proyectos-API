package TestsProyecto;

import aplicacion.Aplicacion;
import controladores.ProyectoController;
import modelo.Proyecto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackageClasses = { Proyecto.class, ProyectoController.class, Aplicacion.class})
@PropertySource("classpath:application.properties")
public class ConfiguradorContextoCucumber {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();

    }
}