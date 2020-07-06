package TestsProyecto;

import excepciones.RestriccionDeEstadoException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.*;

import io.cucumber.datatable.DataTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class StepDefGestionarProyecto extends SpringTest {

    private Proyecto proyecto;
    private String estado;
    private Map<String,Long> diccionario_nombre_id = new HashMap<>();
    private Exception excepcion;

    @Given("un listado con proyectos cargados")
    public void unListadoConProyectosCargados(DataTable dt) {
        List<Map<String,String>> listaDeMapas = dt.asMaps();
        for (Map<String, String> fila : listaDeMapas) {
            Proyecto proyecto;
            Proyecto proyecto_guardado;
            String nombre = fila.get("nombre");
            if (fila.get("tipo").equals("Desarrollo")) {
                proyecto = new ProyectoDeDesarrollo(nombre);
            }
            else { proyecto = new ProyectoDeImplementacion(nombre); }
            proyecto_guardado = proyectoService.save(proyecto);
            diccionario_nombre_id.put(fila.get("nombre"),proyecto_guardado.getId());
        }
    }

    @Given("selecciono el proyecto {string}")
    public void seleccionoElProyecto(String nombreDeProyecto) {
        long id = diccionario_nombre_id.get(nombreDeProyecto);
        this.proyecto =  proyectoService.getOne(id);;
    }

    @When("modifico su estado a {string}")
    public void modificoSuEstadoA(String nuevoEstado) {
        this.proyecto.setEstado(nuevoEstado);
        this.estado = nuevoEstado;
    }

    @Then("el estado del proyecto es el correcto")
    public void elEstadoDelProyectoEsElCorrecto() {
        assertEquals(estado,proyecto.getEstado());
    }

    @When("asigno la fecha de inicio a {string}")
    public void asignoLaFechaDeInicioA(String fecha) {
        try {
            proyecto.setFechaDeInicio(fecha);
        } catch (ParseException|RestriccionDeEstadoException e) {
            this.excepcion = e;
        }
    }

    @Then("la fecha de inicio del proyecto es {string}")
    public void laFechaDeInicioDelProyectoEs(String fecha) {
        try {
            assertEquals(new SimpleDateFormat("MM-dd-yyyy").parse(fecha), proyecto.getFechaDeInicio());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Given("selecciono un proyecto y le asigno la fecha de inicio {string}")
    public void seleccionoUnProyectoYLeAsignoLaFechaDeInicio(String fecha) throws ParseException {
        this.proyecto = new ProyectoDeDesarrollo("Proyecto X");
        excepcion = null;
        this.proyecto.setFechaDeInicio(fecha);
    }

    @Then("se lanza un error indicando que la fecha de inicio no se puede modificar")
    public void seLanzaUnErrorIndicandoQueLaFechaDeInicioNoSePuedeModificar() {
        assertNotNull(excepcion);
        assertEquals(excepcion.getClass(), RestriccionDeEstadoException.class);
    }

    @Then("se lanza un error indicando que la fecha de inicio ingresada no es válida")
    public void seLanzaUnErrorIndicandoQueLaFechaDeInicioIngresadaNoEsVálida() {
        assertNotNull(excepcion);
        assertEquals(excepcion.getClass(), ParseException.class);
    }

    @Given("selecciono un proyecto")
    public void seleccionoUnProyecto() {
        this.proyecto = new ProyectoDeDesarrollo("Proyecto X");
        excepcion = null;
    }

    @Given("creo un proyecto con fecha de inicio {string}")
    public void creoUnProyectoConFechaDeInicio(String fecha) {
        this.proyecto = new ProyectoDeDesarrollo("Proyecto Y");
        try {
            this.proyecto.setFechaDeInicio(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @When("lo guardo en el repositorio")
    public void loGuardoEnElRepositorio() {
        proyecto = proyectoService.saveNew(proyecto);
    }

    @Then("la fecha se guardo correctamente")
    public void laFechaSeGuardoCorrectamente() {
        Proyecto proyectoGuardado = proyectoService.getOne(proyecto.getId());
        assertEquals(proyecto.getFechaDeInicio().getClass(), Date.class);
        assertEquals(proyecto.getFechaDeInicio(),proyectoGuardado.getFechaDeInicio());
    }

    @And("cambio el estado de proyecto a activo")
    public void cambioElEstadoDeProyectoAIniciado() {
        this.proyecto.setEstado("Activo");
    }




    @Given("selecciono el proyecto {string} con estado {string}")
    public void seleccionoElProyectoConUnEstado(String nombreDeProyecto,String estado) {
        long id = diccionario_nombre_id.get(nombreDeProyecto);
        Proyecto proyecto = proyectoService.getOne(id);
        proyecto.setEstado(estado);
        this.proyecto = proyecto;
    }

    @Then("el estado del proyecto sigue siendo {string}")
    public void elEstadoDelProyectoSigueSiendoElMismo(String estadoEsperado) {
        assertTrue(this.proyecto.getEstado().equals(estadoEsperado));
    }

    @When("le cambio el nombre a {string} y descripcion {string}")
    public void leCambioElNombreAYDescripcion(String nuevoNombre, String nuevaDescripcion) {
        proyecto.setNombre(nuevoNombre);
        proyecto.setDescripcion(nuevaDescripcion);
    }

    @Then("el nombre del proyecto es {string}")
    public void elNombreDelProyectoEs(String nombreCorrecto) { assertEquals(nombreCorrecto,proyecto.getNombre()); }

    @And("la descripción es {string}")
    public void laDescripciónEs(String descripcion) { assertEquals(descripcion,proyecto.getDescripcion()); }

    @Given("selecciono un proyecto de Desarrollo")
    public void seleccionoUnProyectoDeDesarrollo() {
        this.proyecto = new ProyectoDeDesarrollo("Proyecto Y");
    }

    @When("lo asocio al producto {string} y lo guardo")
    public void loAsocioAlProductoYLoGuardo(String producto) {
        ((ProyectoDeDesarrollo)proyecto).setProducto(producto);
        proyecto = proyectoService.saveNew(proyecto);
    }

    @Then("el proyecto tiene el producto asociado {string}")
    public void elProyectoTieneElProductoAsociado(String producto) {
        ProyectoDeDesarrollo proyectoDeDesarrollo = (ProyectoDeDesarrollo) proyectoService.getOne(proyecto.getId());
        assertEquals(proyectoDeDesarrollo.getProducto(), producto);
    }
}
