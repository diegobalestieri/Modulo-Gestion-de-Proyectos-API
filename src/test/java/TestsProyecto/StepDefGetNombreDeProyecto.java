package TestsProyecto;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Proyecto;

import static org.junit.Assert.assertEquals;


public class StepDefGetNombreDeProyecto {
    private String nombreOriginal;
    private String nombreDevuelto;
    private Proyecto proyecto;


    @Given("un proyecto creado con cierto nombre {string}")
    public void creoUnProyectoconNombre(String nombre) {
        proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        nombreOriginal = nombre;
    }

    @When("pregunto el nombre del proyecto")
    public void getNombreDeProyecto() { nombreDevuelto = proyecto.getNombre(); }

    @Then("se me devuelve el nombre correcto")
    public void elNombreDelProyectoEsCorrecto() { assertEquals(nombreOriginal,nombreDevuelto); }
}
