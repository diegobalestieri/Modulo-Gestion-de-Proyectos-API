package TestsProyecto;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Fase;
import modelo.Proyecto;
import modelo.ProyectoDeDesarrollo;
import modelo.ProyectoDeImplementacion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StepDefCrearFase extends SpringTest{

    private Proyecto proyecto;

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
}
