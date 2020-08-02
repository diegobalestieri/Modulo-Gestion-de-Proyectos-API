package TestsProyecto;

import com.fasterxml.jackson.annotation.JsonInclude;
import excepciones.TipoDeProyectoInvalido;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Estado.EstadoProyecto;
import modelo.Fase;
import modelo.Proyecto;
import modelo.Tarea;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefGestionarFase extends SpringTest{

    private Proyecto proyecto;
    private Fase fase;
    private List<String> ids = new ArrayList<>();

    @When("creo una fase para el proyecto con los siguientes datos y lo guardo")
    public void creoUnaFaseParaElProyectoConLosSiguientesDatosYLoGuardo(DataTable dt) throws ParseException {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        for (int i = 0; i < list.size(); i++){
            Fase aux = new Fase();
            aux.setNombre( list.get(i).get("nombre"));
            aux.setDescripcion(list.get(i).get("descripcion"));
            aux.asignarFechaDeInicio(list.get(i).get("fecha de inicio"));
            aux.asignarFechaDeFinalizacion(list.get(i).get("fecha de finalizacion"));
            boolean res = proyecto.crearFase(aux);
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
    public void creoUnaFaseParaElProyectoConLosSiguientesDatos(DataTable dt) throws ParseException {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        for (int i = 0; i < list.size(); i++){
            Fase aux = new Fase();
            aux.setNombre( list.get(i).get("nombre"));
            aux.setDescripcion(list.get(i).get("descripcion"));
            aux.asignarFechaDeInicio(list.get(i).get("fecha de inicio"));
            aux.asignarFechaDeFinalizacion(list.get(i).get("fecha de finalizacion"));
            boolean res = proyecto.crearFase(aux);
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
    public void cuentoConUnProyectoIniciado() throws TipoDeProyectoInvalido {
        proyecto = new Proyecto();
        proyecto.setTipoDeProyecto("Implementación");
        proyecto.setNombre("Proyecto X");
        proyecto.setEstado("Activo");
    }

    @Before
    public void setup() {
        mapper.setDateFormat(this.df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
    }


    @Given("tengo el siguiente proyecto creado")
    public void tengoElSiguienteProyectoCreado(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        this.ids.clear();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("tipo").equals("Desarrollo")) {
                proyecto = new Proyecto();
                proyecto.setTipoDeProyecto("Desarrollo");
            } else {
                proyecto = new Proyecto();
                proyecto.setTipoDeProyecto("Implementación");
            }
            proyecto.setNombre("Proyecto de Desarrollo");
            proyecto.setDescripcion(list.get(i).get("descripcion"));
            proyecto.setEstado("Activo");
            String requestJson = mapper.writeValueAsString(proyecto);
            System.out.print(requestJson);
            MvcResult requestResult = this.mockMvc.perform(post("/proyectos")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(requestJson)).andReturn();
            String response = requestResult.getResponse().getContentAsString();
            proyecto.setId(Long.valueOf(obtenerId(response)));
        }
    }

    @When("creo una fase para el proyecto desde la API con los siguientes datos")
    public void creoUnaFaseParaElProyectoDesdeLaAPIConLosSiguientesDatos(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        String url = "/proyectos/{id}/fases";
        String url_aux;
        Fase fase;
        for (int i = 0; i < list.size(); i++) {
            fase = new Fase();
            fase.setNombre(list.get(i).get("nombre"));
            fase.setDescripcion(list.get(i).get("descripcion"));
            fase.asignarFechaDeInicio(list.get(i).get("fecha de inicio"));
            fase.asignarFechaDeFinalizacion(list.get(i).get("fecha de finalizacion"));
            String requestJson = mapper.writeValueAsString(fase);
            url_aux = url.replace("{id}", String.valueOf(proyecto.getId()));
            MvcResult requestResult = this.mockMvc.perform(post(url_aux)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(list.get(i).get("nombre")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value(list.get(i).get("descripcion")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeInicio").value(list.get(i).get("fecha de inicio") + "T00:00:00.000+00:00")) //MUY HARDCODEADO
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeFinalizacion").value(list.get(i).get("fecha de finalizacion") + "T00:00:00.000+00:00"))
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
        for (int i = 0; i < list.size(); i++) {
            url_aux = url.replace("{id}", String.valueOf(proyecto.getId()));
            url_aux = url_aux + "/" + ids.get(i);
            System.out.print('\n' + url_aux + '\n');
            MvcResult requestResult = this.mockMvc.perform(get(url_aux)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(list.get(i).get("nombre")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value(list.get(i).get("descripcion")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeInicio").value(list.get(i).get("fecha de inicio") + "T00:00:00.000+00:00"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeFinalizacion").value(list.get(i).get("fecha de finalizacion") + "T00:00:00.000+00:00"))
                    .andReturn();
        }
    }

    @And("cuando la elimino deja de existir en el proyecto")
    public void cuandoLaEliminoDejaDeExistirEnElProyecto(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        String url = "/proyectos/{id}/fases";
        String url_aux;
        for (int i = 0; i < list.size(); i++) {
            url_aux = url.replace("{id}", String.valueOf(proyecto.getId()));
            url_aux = url_aux + "/" + ids.get(i);
            System.out.print('\n' + url_aux + '\n');
            MvcResult requestResult = this.mockMvc.perform(delete(url_aux)
                    .contentType(MediaType.APPLICATION_JSON))
                    //.param("id_fase", ids.get(i)))
                    .andExpect(status().isOk())
                    .andReturn();
            requestResult = this.mockMvc.perform(get(url_aux)
                    .contentType(MediaType.APPLICATION_JSON))
                    //.param("id_fase", ids.get(i)))
                    .andExpect(status().isNotFound())
                    .andReturn();
        }
    }

    @And("modifico el nombre, descripción, fecha de inicio o finalización de una fase ya creada")
    public void modificoElNombreDescripciónFechaDeInicioOFinalizaciónDeUnaFaseYaCreada(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        String url = "/proyectos/{id}/fases";
        String url_aux;
        Fase fase;
        for (int i = 0; i < list.size(); i++) {
            fase = new Fase();
            fase.setNombre(list.get(i).get("nombre"));
            fase.setDescripcion(list.get(i).get("descripcion"));
            fase.asignarFechaDeInicio(list.get(i).get("fecha de inicio"));
            fase.asignarFechaDeFinalizacion(list.get(i).get("fecha de finalizacion"));
            String requestJson = mapper.writeValueAsString(fase);
            url_aux = url.replace("{id}", String.valueOf(proyecto.getId()));
            url_aux = url_aux + "/" + ids.get(i);
            MvcResult requestResult = this.mockMvc.perform(put(url_aux)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(list.get(i).get("nombre")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value(list.get(i).get("descripcion")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeInicio").value(list.get(i).get("fecha de inicio") + "T00:00:00.000+00:00")) //MUY HARDCODEADO
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeFinalizacion").value(list.get(i).get("fecha de finalizacion") + "T00:00:00.000+00:00"))
                    .andReturn();
            String response = requestResult.getResponse().getContentAsString();
            this.ids.add(obtenerId(response));
        }
    }

    @Given("cuento con un proyecto cargado con fecha de inicio {string}")
    public void cuentoConUnProyectoCargadoConFechaDeInicio(String fecha) throws Exception {
        setup();
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto ERP");
        proyecto.setTipoDeProyecto("Implementación");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        proyecto.setFechaDeInicio(sdf.parse(fecha));
        proyecto.setEstado(EstadoProyecto.ACTIVO);
        String requestJson = mapper.writeValueAsString(proyecto);
        MvcResult requestResult = this.mockMvc.perform(post("/proyectos")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        this.proyecto.setId(Long.valueOf(obtenerId(response)));
    }

    @When("creo una fase en el proyecto con una fecha de inicio {string}")
    public void creoUnaFaseEnElProyectoYLeAsignoUnaFechaDeInicio(String fecha) throws Exception {
        fase = new Fase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fase.setFechaDeInicio(sdf.parse(fecha));
        fase.setNombre("Fase 1");
        String requestJson = mapper.writeValueAsString(fase);
        String url = "/proyectos/" + proyecto.getId() + "/fases";
        MvcResult requestResult = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        this.fase.setId(Long.valueOf(obtenerId(response)));
    }

    @Then("se lanza un error indicando que la fecha de inicio de la fase no puede ser anterior a la del proyecto que la contiene")
    public void seLanzaUnErrorIndicandoQueLaFechaDeInicioDeLaFaseNoPuedeSerAnteriorALaDelProyectoQueLaContiene() {
    }

    @And("la fase no se crea")
    public void laFaseNoSeCrea() throws Exception {
        String url = "/proyectos/" + proyecto.getId() + "/fases";
        MvcResult requestResult = this.mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        assertEquals(response, "[]");
    }

    @When("modifico la fecha de inicio de la fase a {string}")
    public void modificoLaFechaDeInicioDeLaFaseA(String fecha) throws Exception {
        Fase aux = new Fase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        aux.setFechaDeInicio(sdf.parse(fecha));
        aux.setNombre("Fase 1");
        String requestJson = mapper.writeValueAsString(aux);
        String url = "/proyectos/" + proyecto.getId() + "/fases/" + fase.getId();
        MvcResult requestResult = this.mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @And("la fecha de inicio de la fase es {string}")
    public void laFechaDeInicioDeLaFaseEs(String fecha) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaAux = sdf.parse(fecha);
        String url = "/proyectos/" + proyecto.getId() + "/fases/" + fase.getId();
        MvcResult requestResult = this.mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        Fase nuevaFase = mapper.readValue(response,Fase.class);
        assertEquals(fechaAux, nuevaFase.getFechaDeInicio());
    }

    @When("creo una fase en el proyecto con una fecha de inicio invalida {string}")
    public void creoUnaFaseEnElProyectoConUnaFechaDeInicioInvalida(String fecha) throws Exception {
        Fase fase = new Fase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fase.setFechaDeInicio(sdf.parse(fecha));
        String requestJson = mapper.writeValueAsString(fase);
        String url = "/proyectos/" + proyecto.getId() + "/fases";
        MvcResult requestResult = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
