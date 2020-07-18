package TestsProyecto;

import com.fasterxml.jackson.annotation.JsonInclude;
import excepciones.TipoDeProyectoInvalido;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Fase;
import modelo.Proyecto;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefCrearFase extends SpringTest{

    private Proyecto proyecto;
    private List<String> ids = new ArrayList<>();

    @When("creo una fase para el proyecto con los siguientes datos y lo guardo")
    public void creoUnaFaseParaElProyectoConLosSiguientesDatosYLoGuardo(DataTable dt) throws ParseException {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        for (int i = 0; i < list.size(); i++){
            Fase aux = new Fase();
            aux.setNombre( list.get(i).get("nombre"));
            aux.setDescripcion(list.get(i).get("descripcion"));
            aux.setFechaDeInicio(list.get(i).get("fecha de inicio"));
            aux.setFechaDeFinalizacion(list.get(i).get("fecha de finalizacion"));
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
            aux.setFechaDeInicio(list.get(i).get("fecha de inicio"));
            aux.setFechaDeFinalizacion(list.get(i).get("fecha de finalizacion"));
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
            proyecto.setNombre(list.get(i).get("nombre"));
            proyecto.setDescripcion(list.get(i).get("descripcion"));
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
            fase.setFechaDeInicio(list.get(i).get("fecha de inicio"));
            fase.setFechaDeFinalizacion(list.get(i).get("fecha de finalizacion"));
            String requestJson = mapper.writeValueAsString(fase);
            url_aux = url.replace("{id}", String.valueOf(proyecto.getId()));
            MvcResult requestResult = this.mockMvc.perform(post(url_aux)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(list.get(i).get("nombre")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value(list.get(i).get("descripcion")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeInicio").value(list.get(i).get("fecha de inicio") + "T03:00:00.000+00:00")) //MUY HARDCODEADO
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeFinalizacion").value(list.get(i).get("fecha de finalizacion") + "T03:00:00.000+00:00"))
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
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(list.get(i).get("nombre")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value(list.get(i).get("descripcion")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeInicio").value(list.get(i).get("fecha de inicio") + "T03:00:00.000+00:00"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeFinalizacion").value(list.get(i).get("fecha de finalizacion") + "T03:00:00.000+00:00"))
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
            fase.setFechaDeInicio(list.get(i).get("fecha de inicio"));
            fase.setFechaDeFinalizacion(list.get(i).get("fecha de finalizacion"));
            String requestJson = mapper.writeValueAsString(fase);
            url_aux = url.replace("{id}", String.valueOf(proyecto.getId()));
            url_aux = url_aux + "/" + ids.get(i);
            MvcResult requestResult = this.mockMvc.perform(put(url_aux)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(list.get(i).get("nombre")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value(list.get(i).get("descripcion")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeInicio").value(list.get(i).get("fecha de inicio") + "T03:00:00.000+00:00")) //MUY HARDCODEADO
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fechaDeFinalizacion").value(list.get(i).get("fecha de finalizacion") + "T03:00:00.000+00:00"))
                    .andReturn();
            String response = requestResult.getResponse().getContentAsString();
            this.ids.add(obtenerId(response));
        }
    }
}
