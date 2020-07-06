package TestsProyecto;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Proyecto;
import modelo.ProyectoDeImplementacion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StepDefEliminarProyectos extends SpringTest{

    private Proyecto proyecto_guardado;
    private Map<String,Long> diccionario_nombre_id = new HashMap();

    @Before
    public void setup() {
        proyectoService.deleteAll();
    }
    @Given("existen {int} proyectos cargados en el sistema")
    public void existenProyectosCargadosEnElSistema(int cantidadDeProyectos) {
        Proyecto proyecto;
        for (int i = 1; i <= cantidadDeProyectos; i++) {
            proyecto = new ProyectoDeImplementacion("Proyecto "+i);
            proyecto_guardado = proyectoService.save(proyecto);
            diccionario_nombre_id.put("Proyecto "+i,proyecto_guardado.getId());
        }
    }

    @When("elimino {int} proyectos")
    public void eliminoProyectos(int cantidadAEliminar) {
        List<Proyecto> lista = proyectoService.findAll();
        for (int i = 1; i <= cantidadAEliminar; i++) {
            long id = diccionario_nombre_id.get("Proyecto "+i);
            proyectoService.deleteById(id);
        }
    }

    @Then("quedan {int} elementos cargados en el sistema")
    public void quedanElementosCargadosEnElSistema(int cantidadDeProyectosRestantes) {
        assertEquals(cantidadDeProyectosRestantes, proyectoService.findAll().size());
    }

}
