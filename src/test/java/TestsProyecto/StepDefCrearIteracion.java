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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class StepDefCrearIteracion extends SpringTest {

    private Proyecto proyecto;

    private long id_proyecto;
    private List<String> ids = new ArrayList<>();
    private List<Iteracion> listaDeIteraciones = new ArrayList();

    @Before
    public void setup() {
        mapper.setDateFormat(this.df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Given("cuento con un proyecto activo con una fase")
    public void cuentoConUnProyectoActivoConUnaFase() {
        this.proyecto = proyectoService.save(new Proyecto());
        this.id_proyecto = proyecto.getId();
        this.proyecto.crearFase(new Fase());
    }

    @And("tengo una lista de iteraciones con los siguientes datos")
    public void tengoUnaListaDeIteraciones(DataTable dt) throws ParseException {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        for (Map<String, String> fila : list) {
            Iteracion iteracion = new Iteracion();
            iteracion.setNombre(fila.get("nombre"));
            iteracion.setFechaDeInicio(fila.get("fecha de inicio"));
            iteracion.setFechaDeFinalizacion(fila.get("fecha de finalizacion"));
            this.listaDeIteraciones.add(iteracion);
        }
    }

    @When("agrego las iteraciones a la fase")
    public void agregoLasIteracionesALaFase() {
        Proyecto proyecto = proyectoService.getOne(id_proyecto);
        Fase fase = proyecto.getFases().get(0);
        for (Iteracion iteracion : listaDeIteraciones) {
            fase.agregarIteracion(iteracion);
        }
    }

    @Then("las iteraciones se agregaron correctamente")
    public void lasIteracionesSeAgregaronCorrectamente() {
        Proyecto proyecto = proyectoService.getOne(id_proyecto);
        Fase fase = proyecto.getFases().get(0);
        for (int i = 0; i < listaDeIteraciones.size(); i++) {
            Iteracion iteracionNoGuardada = listaDeIteraciones.get(i);
            Iteracion iteracionGuardada = fase.getIteraciones().get(i);
            assertEquals(iteracionNoGuardada.getNombre(),iteracionGuardada.getNombre());
            assertEquals(iteracionNoGuardada.getFechaDeInicio(),iteracionGuardada.getFechaDeInicio());
            assertEquals(iteracionNoGuardada.getFechaDeFinalizacion(),iteracionGuardada.getFechaDeFinalizacion());
        }
    }
}
