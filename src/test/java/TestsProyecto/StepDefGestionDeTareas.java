package TestsProyecto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import excepciones.TipoDeProyectoInvalido;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Proyecto;
import modelo.Tarea;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefGestionDeTareas extends SpringTest{
    private String idProyecto;
    private List<String> idsTareas;
    private String urlPostTarea = "/proyectos/{id}/tareas";
    public void setup() {
        mapper.setDateFormat(this.df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Given("tengo un proyecto creado")
    public void tengoUnProyectoCreado() throws Exception {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto ERP");
        proyecto.setTipoDeProyecto("Implementación");
        String requestJson = mapper.writeValueAsString(proyecto);
        MvcResult requestResult = this.mockMvc.perform(post("/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(requestJson)).andReturn();
        String response = requestResult.getResponse().getContentAsString();
        idProyecto = obtenerId(response);
    }

    @When("creo una tarea con fecha de finalización, nombre, descripción y prioridad")
    public void creoUnaTareaConFechaDeFinalizaciónNombreDescripciónYPrioridad(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        String aux = urlPostTarea.replace("{id}", idProyecto);
        for (Map<String, String> stringStringMap : list) {
            Tarea tarea = new Tarea();
            tarea.setNombre(stringStringMap.get("nombre"));
            tarea.setDescripcion(stringStringMap.get("descripcion"));
            tarea.setPrioridad(stringStringMap.get("descripcion"));
            tarea.setFechaDeFinalizacion(stringStringMap.get("fecha de fin"));
            String requestJson = mapper.writeValueAsString(tarea);
            MvcResult requestResult = this.mockMvc.perform(post(aux)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(requestJson)).andReturn();
            String response = requestResult.getResponse().getContentAsString();
            idsTareas.add(obtenerId(response));
        }
    }

    @Then("la tarea se agrega al proyect backlog")
    public void laTareaSeAgregaAlProyectBacklog() throws Exception {
        String aux = urlPostTarea.replace("{id}", idProyecto);
        MvcResult requestResult = this.mockMvc.perform(get(aux)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("id", "id"))
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        //assertEquals(obtenerId(response), id);
    }

    @And("contiene los datos correspondientes")
    public void contieneLosDatosCorrespondientes() {

    }

    @And("su estado es “No iniciado”")
    public void suEstadoEsNoIniciado() {

    }

    @And("su fecha de creación es la del día de la fecha en que fue creada")
    public void suFechaDeCreaciónEsLaDelDíaDeLaFechaEnQueFueCreada() {

    }
}
