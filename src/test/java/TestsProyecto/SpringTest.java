package TestsProyecto;

import aplicacion.Aplicacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import modelo.Proyecto;
import modelo.ProyectoDeDesarrollo;
import modelo.ProyectoDeImplementacion;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import servicio.ProyectoService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@AutoConfigureMockMvc
@ContextConfiguration(classes = Aplicacion.class)
public class SpringTest {
    @Autowired
    protected ProyectoService proyectoService;
    @Autowired
    MockMvc mockMvc;
    protected ObjectMapper mapper = new ObjectMapper();
    protected DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public String obtenerId(String response){
            int inicio = response.indexOf("id");
            int fin = response.indexOf(',',inicio);
        return response.substring(inicio+4,fin);
    }

}