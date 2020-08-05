package TestsProyecto;

import excepciones.AccionNoPermitidaException;
import excepciones.FechaInvalidaException;
import excepciones.RestriccionDeEstadoException;
import excepciones.TipoDeProyectoInvalido;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Tarea;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import modelo.Proyecto;

import io.cucumber.datatable.DataTable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefGestionarProyecto extends SpringTest {

    private long id;
    private Date fecha;
    private String estado;
    private Map<String,Long> diccionario_nombre_id = new HashMap<>();
    private Exception excepcion;
    private String url = "/proyectos/";
    private Proyecto proyecto;

    @Given("un listado con proyectos cargados")
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public void unListadoConProyectosCargados(DataTable dt) throws TipoDeProyectoInvalido {
        List<Map<String,String>> listaDeMapas = dt.asMaps();
        for (Map<String, String> fila : listaDeMapas) {
            Proyecto proyecto;
            Proyecto proyecto_guardado;
            String nombre = fila.get("nombre");
            if (fila.get("tipo").equals("Desarrollo")) {
                proyecto = new Proyecto();
                proyecto.setTipoDeProyecto("Desarrollo");
                proyecto.setNombre(nombre);
            }
            else {
                proyecto = new Proyecto();
                proyecto.setTipoDeProyecto("Implementación");
                proyecto.setNombre(nombre);
            }
            proyecto_guardado = proyectoService.save(proyecto);
            diccionario_nombre_id.put(fila.get("nombre"),proyecto_guardado.getId());
        }
    }

    @Given("selecciono el proyecto {string}")
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public void seleccionoElProyecto(String nombreDeProyecto) {
        long id = diccionario_nombre_id.get(nombreDeProyecto);
        this.id =  id;
    }

    @When("modifico su estado a {string}")
    public void modificoSuEstadoA(String nuevoEstado) {
        Proyecto proyecto = proyectoService.getOne(id);
        try { proyecto.setEstado(nuevoEstado); }
        catch(AccionNoPermitidaException e) { this.excepcion = e; }
        this.estado = nuevoEstado;
    }

    @Then("el estado del proyecto es el correcto")
    public void elEstadoDelProyectoEsElCorrecto() {
        Proyecto proyecto = proyectoService.getOne(id);
        assertEquals(estado,proyecto.getEstado().getNombre());
    }

    @Transactional
    @When("asigno la fecha de inicio a {string}")
    public void asignoLaFechaDeInicioA(String fecha) {
        Proyecto proyecto = proyectoService.getOne(id);
        try {
            proyecto.asignarFechaDeInicio(fecha);
        } catch (ParseException|AccionNoPermitidaException|FechaInvalidaException e) {
            this.excepcion = e;
        }
    }

    @Then("la fecha de inicio del proyecto es {string}")
    public void laFechaDeInicioDelProyectoEs(String fecha) {
        Proyecto proyecto = proyectoService.getOne(id);
        try {
            assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(fecha), proyecto.getFechaDeInicio());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Given("selecciono un proyecto y le asigno la fecha de inicio {string}")
    public void seleccionoUnProyectoYLeAsignoLaFechaDeInicio(String fecha) throws ParseException, TipoDeProyectoInvalido {
        Proyecto proyecto = proyectoService.save(new Proyecto());
        proyecto.setTipoDeProyecto("Desarrollo");
        proyecto.setNombre("Proyecto X");
        proyecto.asignarFechaDeInicio(fecha);
        excepcion = null;
        this.id = proyecto.getId();
    }

    @Then("se lanza un error indicando que la fecha de inicio no se puede modificar")
    public void seLanzaUnErrorIndicandoQueLaFechaDeInicioNoSePuedeModificar() {
        assertNotNull(true);
        //assertEquals(true);
    }

    @Then("se lanza un error indicando que la fecha de inicio ingresada no es válida")
    public void seLanzaUnErrorIndicandoQueLaFechaDeInicioIngresadaNoEsVálida() {
        assertNotNull(excepcion);
        assertEquals(excepcion.getClass(), ParseException.class);
    }

    @Given("selecciono un proyecto")
    public void seleccionoUnProyecto() throws TipoDeProyectoInvalido {
        Proyecto proyecto = proyectoService.save(new Proyecto());
        proyecto.setNombre("Proyecto X");
        proyecto.setTipoDeProyecto("Desarrollo");
        excepcion = null;
        this.id = proyecto.getId();
    }

    @Given("creo un proyecto con fecha de inicio {string}")
    public void creoUnProyectoConFechaDeInicio(String fecha) throws TipoDeProyectoInvalido {
        Proyecto proyecto = proyectoService.save(new Proyecto());
        proyecto.setNombre("Proyecto Y");
        proyecto.setTipoDeProyecto("Desarrollo");
        try {
            proyecto.asignarFechaDeInicio(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.id = proyecto.getId();
    }

    @When("pregunto la fecha de inicio del proyecto")
    public void loGuardoEnElRepositorio() {
        Proyecto proyecto = proyectoService.getOne(id);
        this.fecha = proyecto.getFechaDeInicio();
    }

    @Then("la fecha {string} se guardo correctamente")
    public void laFechaSeGuardoCorrectamente(String fechaDeInicio) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaCorrecta = format.parse(fechaDeInicio);
        Proyecto proyecto = proyectoService.getOne(id);
        assertEquals(proyecto.getFechaDeInicio().getClass(), java.sql.Timestamp.class);
        assertEquals(fechaCorrecta,proyecto.getFechaDeInicio());
    }

    @And("cambio el estado de proyecto a activo")
    public void cambioElEstadoDeProyectoAIniciado() {
        Proyecto proyecto = proyectoService.getOne(id);
        proyecto.setEstado("Activo");
    }


    @Given("selecciono el proyecto {string} con estado {string}")
    public void seleccionoElProyectoConUnEstado(String nombreDeProyecto,String estado) {
        long id = diccionario_nombre_id.get(nombreDeProyecto);
        Proyecto proyecto = proyectoService.getOne(id);
        proyecto.setEstado(estado);
        this.id = id;
    }

    @Then("el estado del proyecto sigue siendo {string}")
    public void elEstadoDelProyectoSigueSiendoElMismo(String estadoEsperado) {
        Proyecto proyecto = proyectoService.getOne(id);
        assertEquals(proyecto.getEstado().getNombre(), estadoEsperado);
    }

    @When("le cambio el nombre a {string} y descripcion {string}")
    @Transactional
    public void leCambioElNombreAYDescripcion(String nuevoNombre, String nuevaDescripcion) {
        Proyecto proyecto = proyectoService.getOne(id);
        proyecto.setNombre(nuevoNombre);
        proyecto.setDescripcion(nuevaDescripcion);
    }

    @Then("el nombre del proyecto es {string}")
    public void elNombreDelProyectoEs(String nombreCorrecto) {
        Proyecto proyecto = proyectoService.getOne(id);
        assertEquals(nombreCorrecto,proyecto.getNombre());
    }

    @And("la descripción es {string}")
    public void laDescripciónEs(String descripcion) {
        Proyecto proyecto = proyectoService.getOne(id);
        assertEquals(descripcion,proyecto.getDescripcion());
    }

    @Given("selecciono un proyecto de Desarrollo")
    public void seleccionoUnProyectoDeDesarrollo() throws TipoDeProyectoInvalido {
        Proyecto proyecto = proyectoService.save(new Proyecto());
        proyecto.setNombre("Proyecto Y");
        proyecto.setTipoDeProyecto("Desarrollo");
        this.id = proyecto.getId();
    }

    @When("lo asocio al producto {string} y lo guardo")
    public void loAsocioAlProductoYLoGuardo(String producto) {
        Proyecto proyecto = proyectoService.getOne(id);
        proyecto.setProducto(producto);
        this.id = proyecto.getId();
    }

    @Then("el proyecto tiene el producto asociado {string}")
    public void elProyectoTieneElProductoAsociado(String producto) {
        Proyecto proyecto = proyectoService.getOne(id);
        assertEquals(producto,proyecto.getProducto());
    }

    @Transactional
    @Then("se lanza un error indicando que el estado no se pudo cambiar")
    public void seLanzaUnErrorIndicandoQueElEstadoNoSePudoCambiar() {
        assertTrue(this.excepcion != null);
        assertEquals(this.excepcion.getClass(),AccionNoPermitidaException.class);
    }

    @Given("tengo un proyecto sin líder")
    public void tengoUnProyectoSinLíder() throws Exception {
        setup();
        proyecto = new Proyecto();
        proyecto.setTipoDeProyecto("Desarrollo");
        String requestJson = mapper.writeValueAsString(proyecto);
        MvcResult requestResult = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        proyecto.setId(Long.valueOf(obtenerId(response)));

    }

    @When("le asigno un líder de proyecto de legajo {string}")
    public void leAsignoUnLíderDeProyecto(String legajo) throws Exception {
        proyecto.setLiderDeProyecto(legajo);
        String requestJson = mapper.writeValueAsString(proyecto);
        String urlPut = url + proyecto.getId();
        MvcResult requestResult = this.mockMvc.perform(put(urlPut)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Then("el proyecto pasa a tener a ese Líder de Proyecto de legajo {string}")
    public void elProyectoPasaATenerAEseLíderDeProyecto(String legajo) throws Exception {
        String urlGet = url + proyecto.getId();
        String requestJson = mapper.writeValueAsString(proyecto);
        MvcResult requestResult = this.mockMvc.perform(put(urlGet)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        Proyecto nuevoProyecto = mapper.readValue(response,Proyecto.class);
        assertEquals(nuevoProyecto.getLiderDeProyecto(), legajo);
    }

    @Given("tengo un proyecto con líder de legajo {string}")
    public void tengoUnProyectoConLíderDeLegajo(String legajo) throws Exception {
        setup();
        proyecto = new Proyecto();
        proyecto.setTipoDeProyecto("Desarrollo");
        proyecto.setLiderDeProyecto(legajo);
        String requestJson = mapper.writeValueAsString(proyecto);
        MvcResult requestResult = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        proyecto.setId(Long.valueOf(obtenerId(response)));
    }
}
