package TestsProyecto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Fase;
import modelo.Proyecto;
import modelo.ProyectoDeDesarrollo;
import modelo.ProyectoDeImplementacion;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefCrearFase extends SpringTest{

    private Proyecto proyecto;
    private List<String> ids = new ArrayList<>();

    @When("creo una fase para el proyecto con los siguientes datos y lo guardo")
    public void creoUnaFaseParaElProyectoConLosSiguientesDatosYLoGuardo(DataTable dt) {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        for (int i = 0; i < list.size(); i++){
            boolean res = proyecto.crearFase( list.get(i).get("nombre"),
                    list.get(i).get("descripcion"),
                    list.get(i).get("fecha de inicio"),
                    list.get(i).get("fecha de finalizacion"));
            assertTrue(res);
        }
        proyecto = proyectoService.saveNew(proyecto);
    }

    @Then("la fase guardada se agrega al proyecto con los datos correspondientes.")
    public void laFaseGuardadaSeAgregaAlProyectoConLosDatosCorrespondientes(DataTable dt) throws ParseException {
        List <Fase> fases = proyectoService.getOne(proyecto.getId()).obtenerFases();
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        assertEquals(list.size(), fases.size());
        for (int i = 0; i < list.size(); i++){
            assertEquals(list.get(i).get("nombre"), fases.get(i).getNombre());
            assertEquals(list.get(i).get("descripcion"), fases.get(i).getDescripcion());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).get("fecha de inicio")), fases.get(i).getFechaDeInicio());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).get("fecha de finalizacion")), fases.get(i).getFechaDeFinalizacion());
        }
    }

    @When("creo una fase para el proyecto con los siguientes datos")
    public void creoUnaFaseParaElProyectoConLosSiguientesDatos(DataTable dt) {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        for (int i = 0; i < list.size(); i++){
            boolean res = proyecto.crearFase( list.get(i).get("nombre"),
                    list.get(i).get("descripcion"),
                    list.get(i).get("fecha de inicio"),
                    list.get(i).get("fecha de finalizacion"));
            assertTrue(res);
        }
    }

    @Then("la fase se agrega al proyecto con los datos correspondientes.")
    public void laFaseSeAgregaAlProyectoConLosDatosCorrespondientes(DataTable dt) throws ParseException {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        List<Fase> fases = proyecto.obtenerFases();
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i).get("nombre"), fases.get(i).getNombre());
            assertEquals(list.get(i).get("descripcion"), fases.get(i).getDescripcion());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).get("fecha de inicio")), fases.get(i).getFechaDeInicio());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).get("fecha de finalizacion")), fases.get(i).getFechaDeFinalizacion());
        }
    }



    @Given("cuento con un proyecto activo")
    public void cuentoConUnProyectoIniciado() {
        proyecto = new ProyectoDeImplementacion("Proyecto X");
        proyecto.setEstado("Activo");
    }

    @Before
    public void setup() {
        mapper.setDateFormat(this.df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    @Given("tengo el siguiente proyecto creado")
    public void tengoElSiguienteProyectoCreado(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("tipo").equals("Desarrollo")) {
                proyecto = new ProyectoDeDesarrollo();
            } else {
                proyecto = new ProyectoDeImplementacion();
            }
            proyecto.setNombre(list.get(i).get("nombre"));
            proyecto.setDescripcion(list.get(i).get("descripcion"));
            String requestJson = mapper.writeValueAsString(proyecto);
            System.out.print(requestJson);
            MvcResult requestResult = this.mockMvc.perform(post("/proyectos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(requestJson)).andReturn();
            String response = requestResult.getResponse().getContentAsString();
            proyecto.setId(Long.valueOf(obtenerId(response)));
        }
    }

    @When("when creo una fase para el proyecto desde la API con los siguientes datos")
    public void whenCreoUnaFaseParaElProyectoDesdeLaAPIConLosSiguientesDatos(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        String url = "/proyectos/{id}/fases";
        String url_aux;
        Fase fase;
        for (int i = 0; i < list.size(); i++) {
            fase = new Fase(list.get(i).get("nombre"));
            fase.setDescripcion(list.get(i).get("descripcion"));
            fase.setFechaDeInicio(list.get(i).get("fecha de inicio"));
            fase.setFechaDeFinalizacion(list.get(i).get("fecha de finalizacion"));
            String requestJson = mapper.writeValueAsString(fase);
            url_aux = url.replace("{id}", String.valueOf(proyecto.getId()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSSXXXXX");
            MvcResult requestResult = this.mockMvc.perform(post(url_aux)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(list.get(i).get("nombre")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value(list.get(i).get("descripcion")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeInicio").value(list.get(i).get("fecha de inicio") + "T03:00:00.000+00:00")) //MUY HARDCODEADO
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeFinalizacion").value(new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).get("fecha de finalizacion"))))
                    .andReturn();
            String response = requestResult.getResponse().getContentAsString();
            this.ids.add(obtenerId(response));
        }
    }

    @Then("cuando obtengo la fase a travez de la api tiene los datos correctos")
    public void cuandoObtengoLaFaseATravezDeLaApiTieneLosDatosCorrectos(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        String url = "/proyectos/{id}/fases";
        String url_aux;
        Fase fase;
        for (int i = 0; i < list.size(); i++) {
            url_aux = url.replace("{id}", String.valueOf(proyecto.getId()));
            MvcResult requestResult = this.mockMvc.perform(get(url_aux)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", ids.get(i)))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(list.get(i).get("nombre")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value(list.get(i).get("descripcion")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fecha de inicio").value(list.get(i).get("fecha de inicio")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fecha de finalizacion").value(list.get(i).get("fecha de finalizacion")))
                    .andReturn();
        }
    }
}
