package TestsProyecto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Fase;
import modelo.Iteracion;
import modelo.Proyecto;
import modelo.Tarea;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefGestionarIteracion extends SpringTest {

    private Proyecto proyecto;

    private String idProyecto;
    private String idFase;
    private List<String> idsTareas = new ArrayList<>();
    private List<Iteracion> listaDeIteraciones = new ArrayList();
    private Iteracion iteracion_creada;
    private Iteracion iteracion_obtenida;
    private long idIteracion;
    private List<Tarea> tareas_cargadas = new ArrayList();
    private List<Tarea> tareas_obtenidas = new ArrayList();

    @Before
    public void setup() {
        mapper.setDateFormat(this.df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Given("cuento con un proyecto activo con una fase y tareas cargadas")
    public void cuentoConUnProyectoActivoConUnaFaseYTareasCargadas() throws Exception {
        setup();
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto ERP");
        proyecto.setTipoDeProyecto("Implementación");
        proyecto.setEstado("Activo");
        String requestJson = mapper.writeValueAsString(proyecto);
        MvcResult requestResult = this.mockMvc.perform(post("/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(requestJson)).andReturn();
        String response = requestResult.getResponse().getContentAsString();
        idProyecto = obtenerId(response);

        Fase fase = new Fase();
        fase.setNombre("Fase de Desarrollo");
        requestJson = mapper.writeValueAsString(fase);
        String url_fases = "/proyectos/" + idProyecto + "/fases";
        requestResult = this.mockMvc.perform(post(url_fases)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(requestJson)).andReturn();
        response = requestResult.getResponse().getContentAsString();
        idFase = obtenerId(response);

        Tarea tarea_1 = new Tarea();
        tarea_1.setNombre("Validar requisitos");
        tareas_cargadas.add(tarea_1);
        requestJson = mapper.writeValueAsString(tarea_1);
        String url_tareas = "/proyectos/" + idProyecto + "/tareas";
        requestResult = this.mockMvc.perform(post(url_tareas)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
        response = requestResult.getResponse().getContentAsString();
        idsTareas.add(obtenerId(response));

        Tarea tarea_2 = new Tarea();
        tarea_2.setNombre("Actualizar documentos");
        tareas_cargadas.add(tarea_2);
        requestJson = mapper.writeValueAsString(tarea_2);
        requestResult = this.mockMvc.perform(post(url_tareas)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
        response = requestResult.getResponse().getContentAsString();
        idsTareas.add(obtenerId(response));

    }

    @And("tengo una lista de iteraciones con los siguientes datos")
    public void tengoUnaListaDeIteraciones(DataTable dt) throws ParseException {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        for (Map<String, String> fila : list) {
            Iteracion iteracion = new Iteracion();
            iteracion.setNombre(fila.get("nombre"));
            iteracion.asignarFechaDeInicio(fila.get("fecha de inicio"));
            iteracion.asignarFechaDeFinalizacion(fila.get("fecha de finalizacion"));
            this.listaDeIteraciones.add(iteracion);
        }
    }

    @When("creo una iteracion con los siguientes datos")
    public void creoUnaIteracionConLosSiguientesDatos(DataTable dt) throws Exception {
        List<Map<String,String>> list = dt.asMaps(String.class, String.class);
        Map<String,String> fila = list.get(0);
        Iteracion iteracion = new Iteracion();
        iteracion.setNombre(fila.get("nombre"));
        iteracion.asignarFechaDeInicio(fila.get("fecha de inicio"));
        iteracion.asignarFechaDeFinalizacion(fila.get("fecha de finalizacion"));
        iteracion_creada = iteracion;
        String requestJson = mapper.writeValueAsString(iteracion);

        String url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones";
        MvcResult requestResult = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                //.andExpect(content().json(requestJson))
                .andReturn();

        String response = requestResult.getResponse().getContentAsString();
        Iteracion nuevaIteracion = mapper.readValue(response,Iteracion.class);
        iteracion_obtenida = nuevaIteracion;
        idIteracion = nuevaIteracion.getId();
    }

    @Then("la iteracion cuenta con los datos correctos")
    public void laIteracionCuentaConLosDatosCorrectos() {
        assertEquals(iteracion_creada,iteracion_obtenida);
    }

    @When("modifico los datos de la iteracion")
    public void modificoLosDatosDeLaIteracion(DataTable dt) throws Exception {
        List<Map<String,String>> list = dt.asMaps(String.class, String.class);
        Map<String,String> fila = list.get(0);
        Iteracion iteracion = new Iteracion();
        iteracion.setNombre(fila.get("nombre"));
        iteracion.asignarFechaDeInicio(fila.get("fecha de inicio"));
        iteracion.asignarFechaDeFinalizacion(fila.get("fecha de finalizacion"));
        String requestJson = mapper.writeValueAsString(iteracion);
        iteracion_creada = iteracion;

        String url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones/" + idIteracion;

        MvcResult requestResult = this.mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                //.andExpect(content().json(requestJson))
                .andReturn();

        String response = requestResult.getResponse().getContentAsString();
        Iteracion nuevaIteracion = mapper.readValue(response,Iteracion.class);
        iteracion_obtenida = nuevaIteracion;
        idIteracion = nuevaIteracion.getId();

    }

    @Given("creo una iteracion y le agrego las tareas del proyecto")
    public void creoUnaIteracionYLeAgregoTareas() throws Exception {
        Iteracion iteracion = new Iteracion();
        iteracion.setNombre("Iteracion 1");
        iteracion.asignarFechaDeInicio("2020-04-15");
        iteracion.asignarFechaDeFinalizacion("2020-04-20");
        String url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones";

        String requestJson = mapper.writeValueAsString(iteracion);
        iteracion_creada = iteracion;

        MvcResult requestResult = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                //.andExpect(content().json(requestJson))
                .andReturn();

        String response = requestResult.getResponse().getContentAsString();
        idIteracion = Long.parseLong(obtenerId(response));

        for (int i = 0; i < idsTareas.size(); i++) {

            url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones/" + idIteracion + "/tareas/?id_tarea=";

            String id_tarea = idsTareas.get(i);
            url += id_tarea;
            requestResult = this.mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andReturn();

        }

        url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones/" + idIteracion;

        requestResult = this.mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        response = requestResult.getResponse().getContentAsString();
        Iteracion nuevaIteracion = mapper.readValue(response,Iteracion.class);
        iteracion_obtenida = nuevaIteracion;
    }

    @When("consulto las tareas de la iteracion")
    public void consultoLasTareasDeLaIteracion() throws Exception {
        String url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones/" + idIteracion + "/tareas";

        MvcResult requestResult = this.mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = requestResult.getResponse().getContentAsString();
        List<Tarea> nuevaListaDeTareas = Arrays.asList(mapper.readValue(response,Tarea[].class));
        for (Tarea tarea : nuevaListaDeTareas) {
            tareas_obtenidas.add(tarea);
        }
    }

    @Then("se me devuelven las tareas correctas")
    public void seMeDevuelvenLasTareasCorrectas() {
        assertEquals(tareas_cargadas,tareas_obtenidas);
    }

    @Given("creo una iteracion en una fase")
    public void creoUnaIteracionEnUnaFase() throws Exception {
        Iteracion iteracion = new Iteracion();
        iteracion.setNombre("Iteracion 1");
        iteracion.asignarFechaDeInicio("2020-04-15");
        iteracion.asignarFechaDeFinalizacion("2020-04-20");
        String url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones";
        String requestJson = mapper.writeValueAsString(iteracion);

        MvcResult requestResult = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        String response = requestResult.getResponse().getContentAsString();
        Iteracion nuevaIteracion = mapper.readValue(response,Iteracion.class);
        iteracion_obtenida = nuevaIteracion;
        idIteracion = nuevaIteracion.getId();

    }


    @When("borro la iteracion")
    public void borroLaIteracion() throws Exception {
        String url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones/" + idIteracion;
        MvcResult requestResult = this.mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Then("la fase ya no cuenta con la iteracion")
    public void laFaseYaNoCuentaConLaIteracion() throws Exception {
        String url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones";

        MvcResult requestResult = this.mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = requestResult.getResponse().getContentAsString();
        List<Iteracion> listaDeIteraciones = Arrays.asList(mapper.readValue(response,Iteracion[].class));
        assertFalse(listaDeIteraciones.contains(iteracion_obtenida));

    }

    @When("elimino las tareas de la iteracion")
    public void eliminoLasTareasDeLaIteracion() throws Exception {
        String url;

        for (int i = 0; i < idsTareas.size(); ++i) {
            url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones/" + idIteracion +
                    "/tareas/" + idsTareas.get(i);
            MvcResult requestResult = this.mockMvc.perform(delete(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        }
    }

    @Then("la iteracion no cuenta con las tareas cargadas")
    public void laIteracionNoCuentaConLasTareasCargadas() throws Exception {
        String url = "/proyectos/" + idProyecto + "/fases/" + idFase + "/iteraciones/" + idIteracion;
        MvcResult requestResult = this.mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = requestResult.getResponse().getContentAsString();
        Iteracion nuevaIteracion = mapper.readValue(response,Iteracion.class);
        assertTrue(nuevaIteracion.getIdsTareas().isEmpty());


    }
}
