package TestsProyecto;

import com.fasterxml.jackson.databind.ObjectMapper;
import controladores.ProyectoController;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Proyecto;
import modelo.ProyectoDeDesarrollo;
import modelo.ProyectoDeImplementacion;
import nonapi.io.github.classgraph.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;


public class StepDefProyectoAPI extends SpringTest{


    @When("tengo el listado de proyectos")
    public void tengoUnListadoDeProyectos() {
    }


    @When("Creo un proyecto con nombre, descripcion y tipo")
    public void creoUnProyectoConNombreDescripcionYTipo(DataTable dt) throws IOException {
        URL url = null;
        Proyecto proyecto;
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);

        url = new URL("http://localhost:8080/proyectos");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("User-Agent", "Java client");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream os = con.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).get("tipo").equals("Desarrollo")){
                proyecto = new ProyectoDeDesarrollo();
            } else{
                proyecto = new ProyectoDeImplementacion();
            }
             proyecto.setNombre(list.get(i).get("nombre"));
             proyecto.setDescripcion(list.get(i).get("descripcion"));
             osw.write(mapper.writeValueAsString(proyecto));
        }
        osw.flush();
        osw.close();
        os.close();  //don't forget to close the OutputStream
        con.connect();


    }

    @Then("el proyecto se crea con los datos correctos")
    public void elProyectoSeCreaConLosDatosCorrectos(DataTable dt) throws IOException {
        URL url = null;
        Proyecto proyecto;
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);

        url = new URL("http://localhost:8080/proyectos");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setRequestProperty("User-Agent", "Java client");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setUseCaches(false);
        con.setAllowUserInteraction(false);

    }
}
