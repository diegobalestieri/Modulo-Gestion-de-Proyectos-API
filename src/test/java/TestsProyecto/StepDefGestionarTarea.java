package TestsProyecto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefGestionarTarea extends SpringTest{
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
            tarea.setPrioridad(stringStringMap.get("prioridad"));
            tarea.setFechaDeFinalizacion(stringStringMap.get("fecha de fin"));
            String requestJson = mapper.writeValueAsString(tarea);
            MvcResult requestResult = this.mockMvc.perform(post(aux)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isCreated())
                    .andReturn();
            String response = requestResult.getResponse().getContentAsString();
            Tarea nuevaTarea = mapper.readValue(response,Tarea.class);
            tareas.add(nuevaTarea);
            idsTareas.add(obtenerId(response));
        }
    }

    @Then("la tarea se agrega al proyect backlog")
    public void laTareaSeAgregaAlProyectBacklog() throws Exception {
        assertEquals(tareas.size(), list.size());

    }

    @And("la tarea contiene los datos correspondientes")
    public void laTareacontieneLosDatosCorrespondientes() throws Exception {
        String aux = urlPostTarea.replace("{id}", idProyecto);
        for (int i = 0; i < list.size() ; i++) {
            String get_result = aux + "/" + tareas.get(i).getId();
            MvcResult requestResult = this.mockMvc.perform(get(get_result)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            Tarea tareaAux = new Tarea();
            tareaAux.setNombre(list.get(i).get("nombre"));
            tareaAux.setDescripcion(list.get(i).get("descripcion"));
            tareaAux.setPrioridad(list.get(i).get("prioridad"));
            tareaAux.setFechaDeFinalizacion(list.get(i).get("fecha de fin"));
            assertTrue(tareaAux.equals(tareas.get(i)));
        }
    }

    @And("su estado es “No iniciado”")
    public void suEstadoEsNoIniciado() {
        for (Tarea tarea : tareas){
            assertEquals(tarea.getEstado(), EstadoTarea.NO_INICIADA);
        }

    }

    @And("su fecha de creación es la del día de la fecha en que fue creada")
    public void suFechaDeCreaciónEsLaDelDíaDeLaFechaEnQueFueCreada() {

    }

    @Given("cuento con una tarea cargada en el proyecto")
    public void tengoUnProyectoCreadoConUnaTareaCargada() throws Exception {
        String aux = urlPostTarea.replace("{id}", idProyecto);
        Tarea tarea = new Tarea();
        tarea.setNombre("Actualizar documentacion");
        tarea.setDescripcion("Una descripcion generica");
        tarea.setPrioridad("Muy alta");
        tarea.setFechaDeFinalizacion("2020-08-09");
        String requestJson = mapper.writeValueAsString(tarea);
        MvcResult requestResult = this.mockMvc.perform(post(aux)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        idsTareas.add(obtenerId(response));
    }

    @When("modifico los siguientes datos de la tarea")
    public void modificoLosSiguientesDatosDeLaTarea(DataTable dt) throws Exception {
        list = dt.asMaps(String.class, String.class);
        Map<String,String> fila = list.get(0);
        Tarea tarea = new Tarea(fila.get("nombre"));
        tarea.setDescripcion(fila.get("descripcion"));
        tarea.setFechaDeFinalizacion(fila.get("fecha de fin"));
        tarea.setPrioridad(fila.get("prioridad"));
        String requestJson = mapper.writeValueAsString(tarea);
        String url_put = urlPostTarea.replace("{id}", idProyecto) + "/" + idsTareas.get(0);
        MvcResult requestResult = this.mockMvc.perform(put(url_put)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        Tarea nuevaTarea = mapper.readValue(response,Tarea.class);
        tareas.add(nuevaTarea);
    }

    @When("elimino la tarea")
    public void eliminoLaTarea() throws Exception {
        String url_delete = urlPostTarea.replace("{id}", idProyecto) + "/" + idsTareas.get(0);
        MvcResult requestResult = this.mockMvc.perform(delete(url_delete)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Then("la tarea ya no se encuentra en el proyecto")
    public void laTareaYaNoSeEncuentraEnElProyecto() throws Exception {
        String url_get = urlPostTarea.replace("{id}", idProyecto) + "/" + idsTareas.get(0);
        MvcResult requestResult = this.mockMvc.perform(get(url_get)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
