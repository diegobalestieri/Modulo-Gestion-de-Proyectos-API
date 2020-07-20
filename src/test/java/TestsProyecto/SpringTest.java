package TestsProyecto;

import aplicacion.Aplicacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import servicio.ProyectoService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@AutoConfigureMockMvc
@ContextConfiguration(classes = Aplicacion.class)
@Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
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