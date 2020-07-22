package TestsProyecto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import excepciones.TipoDeProyectoInvalido;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Estado.EstadoTarea;
import modelo.Proyecto;
import modelo.Tarea;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefGestionDeTareas extends SpringTest{
    private String idProyecto;
    private List<String> idsTareas = new ArrayList<>();
    private List<Tarea> tareas = new ArrayList<>();
    private String urlPostTarea = "/proyectos/{id}/tareas";
    private List<Map<String, String>> list;
    public void setup() {
        mapper.setDateFormat(this.df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
    }

    @Given("tengo un proyecto creado")
    public void tengoUnProyectoCreado() throws Exception {
        setup();
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto ERP");
        proyecto.setTipoDeProyecto("Implementación");
        String requestJson = mapper.writeValueAsString(proyecto);
        MvcResult requestResult = this.mockMvc.perform(post("/proyectos")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(requestJson)).andReturn();
        String response = requestResult.getResponse().getContentAsString();
        idProyecto = obtenerId(response);
    }

    @When("creo una tarea con fecha de finalización, nombre, descripción y prioridad")
    public void creoUnaTareaConFechaDeFinalizaciónNombreDescripciónYPrioridad(DataTable dt) throws Exception {
        setup();
        list = dt.asMaps(String.class, String.class);
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
                    .andReturn();
            String response = requestResult.getResponse().getContentAsString();
            tareas.add(mapper.readValue(response, Tarea.class));
            idsTareas.add(obtenerId(response));
        }
    }

    @Then("la tarea se agrega al proyect backlog")
    public void laTareaSeAgregaAlProyectBacklog() throws Exception {
        assertEquals(tareas.size(), list.size());

    }

    @And("contiene los datos correspondientes")
    public void contieneLosDatosCorrespondientes() throws Exception {
        String aux = urlPostTarea.replace("{id}", idProyecto);
        for (int i = 0; i < list.size() ; i++) {
            MvcResult requestResult = this.mockMvc.perform(get(aux + "/" + tareas.get(i).getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            Tarea tareaAux = new Tarea();
            tareaAux.setNombre(list.get(i).get("nombre"));
            tareaAux.setDescripcion(list.get(i).get("descripcion"));
            tareaAux.setPrioridad(list.get(i).get("descripcion"));
            tareaAux.setFechaDeFinalizacion(list.get(i).get("fecha de fin"));
            assertTrue(tareaAux.equals(tareas.get(i)));
        }
    }

    @And("su estado es “No iniciado”")
    public void suEstadoEsNoIniciado() {
        for (Tarea tarea : tareas){
            assertEquals(tarea.getEstado(), EstadoTarea.TO_DO);
        }

    }

    @And("su fecha de creación es la del día de la fecha en que fue creada")
    public void suFechaDeCreaciónEsLaDelDíaDeLaFechaEnQueFueCreada() {

    }
}
