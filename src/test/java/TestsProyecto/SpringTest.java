package TestsProyecto;

import aplicacion.Aplicacion;
import io.cucumber.spring.CucumberContextConfiguration;
import modelo.Proyecto;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import servicio.ProyectoService;

import java.util.List;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@ContextConfiguration(classes = Aplicacion.class)
public class SpringTest {
    @Autowired
    protected ProyectoService proyectoService;

}