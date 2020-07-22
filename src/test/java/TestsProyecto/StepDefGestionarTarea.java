package TestsProyecto;

import com.fasterxml.jackson.annotation.JsonInclude;
import excepciones.FechaInvalidaException;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StepDefGestionarTarea extends SpringTest {

    private Tarea tarea;
    private Exception excepcion;
    private Date fechaDeInicio;
    private Date fechaDeFinalizacion;

    @Before
    public void setup() {
        mapper.setDateFormat(this.df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    @Given("tengo una tarea creada con nombre {string}")
    public void otraTareaCreadaConLosSiguientesDatos(String nombre) {
        this.tarea = new Tarea(nombre);
    }


    @When("pregunto el nombre y el estado de la tarea")
    public void preguntoElNombreYElEstadoDeLaTarea() { }

    @Then("el nombre de la tarea es {string}")
    public void elNombreDeLaTareaEs(String nombreTarea) {
        assertEquals(tarea.getNombre(),nombreTarea);
    }

    @And("el estado de la tarea es {string}")
    public void elEstadoDeLaTareaEs(String estadoTarea) {
        assertEquals(tarea.getEstado().getNombre(),estadoTarea);
    }

    @When("modifico el estado de la tarea a {string}")
    public void modificoElEstadoDeLaTareaA(String nuevoEstado) {
        tarea.setEstado(nuevoEstado);
    }

    @When("modifico el nombre de la tarea a {string}")
    public void modificoElNombreDeLaTareaA(String nuevoNombre) {
        tarea.setNombre(nuevoNombre);
    }

    @And("modifico la descripcion de la tarea a {string}")
    public void modificoLaDescripcionDeLaTareaA(String nuevaDescripcion) {
        tarea.setDescripcion(nuevaDescripcion);
    }

    @And("la descripcion de la tarea es {string}")
    public void laDescripcionDeLaTareaEs(String descripcion) {
        assertEquals(tarea.getDescripcion(),descripcion);
    }

    @When("asigno la fecha de inicio de la tarea a {string}")
    public void asignoLaFechaDeInicioDeLaTareaA(String fechaDeInicio) {
        try {
            tarea.setFechaDeInicio(df.parse(fechaDeInicio));
            this.fechaDeInicio = tarea.getFechaDeInicio();
        }
        catch(ParseException e) { this.excepcion = e; }
    }


    @Then("la tarea tiene la fecha de inicio correcta")
    public void laTareaTieneLaFechaDeInicioCorrecta() {
        assertEquals(this.fechaDeInicio,tarea.getFechaDeInicio());
    }

    @Then("se lanza un error indicando que la fecha ingresada no es valida")
    public void seLanzaUnErrorIndicandoQueLaFechaIngresadaNoEsValida() {
        assertNotNull(this.excepcion);
        assertEquals(this.excepcion.getClass(),ParseException.class);
    }

    @And("la fecha de inicio de la tarea no se modificó")
    public void laFechaDeInicioDeLaTareaNoSeModifico() {
        assertEquals(this.fechaDeInicio,tarea.getFechaDeInicio());
    }

    @When("asigno la fecha de finalizacion de la tarea a {string}")
    public void asignoLaFechaDeFinalizacionDeLaTareaA(String fechaDeFinalizacion) {
        try {
            tarea.setFechaDeFinalizacion(fechaDeFinalizacion);
            this.fechaDeFinalizacion = tarea.getFechaDeFinalizacion();
        }
        catch(ParseException | FechaInvalidaException e) { this.excepcion = e; }
    }

    @Then("la tarea tiene la fecha de finalizacion correcta")
    public void laTareaTieneLaFechaDeFinalizacionCorrecta() {
        assertEquals(tarea.getFechaDeFinalizacion(),this.fechaDeFinalizacion);
    }

    @And("la fecha de finalizacion de la tarea no se modificó")
    public void laFechaDeFinalizacionDeLaTareaNoSeModificó() {
        assertEquals(tarea.getFechaDeFinalizacion(),this.fechaDeFinalizacion);
    }

    @Then("se lanza un error indicando que la fecha de finalizacion no puede ser anterior a la de inicio")
    public void seLanzaUnErrorIndicandoQueLaFechaDeFinalizacionNoPuedeSerAnteriorALaDeInicio() {
        assertNotNull(this.excepcion);
        assertEquals(this.excepcion.getClass(), FechaInvalidaException.class);
    }
}
