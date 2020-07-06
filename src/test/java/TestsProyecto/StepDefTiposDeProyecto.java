package TestsProyecto;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modelo.Proyecto;
import modelo.ProyectoDeDesarrollo;
import modelo.ProyectoDeImplementacion;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StepDefTiposDeProyecto extends SpringTest{

    private Map<String,Long> diccionario_nombre_id = new HashMap<String,Long>();

    @Given("un listado de proyectos")
    public void unListadoVacio() { proyectoService.deleteAll(); }

    @When("creo proyectos de distinto tipo")
    public void creoProyectos(DataTable dt) {
        List<Map<String,String>> listaDeMapas = dt.asMaps();
        Proyecto proyecto;
        Proyecto proyecto_guardado;
        long id;
        for (Map<String,String> fila: listaDeMapas) {
            if (fila.get("tipo").equals("Implementación")) {
                proyecto = new ProyectoDeImplementacion(fila.get("nombre"));
            }
            else {
                proyecto = new ProyectoDeDesarrollo(fila.get("nombre"));
            }
            proyecto_guardado = proyectoService.save(proyecto);
            diccionario_nombre_id.put(fila.get("nombre"),proyecto_guardado.getId());
        }
    }

    @Then("se me devuelven los tipos correctos para cada proyecto")
    public void losTiposDeProyectoSonCorrectos(DataTable dt) {
        List<Map<String, String>> lista = dt.asMaps();
        int diferencias_encontradas = 0;
        for (Map<String, String> fila : lista) {
            Proyecto proyecto = proyectoService.getOne(diccionario_nombre_id.get(fila.get("nombre")));
            assertEquals(fila.get("tipo"),proyecto.getTipoDeProyecto());
        }

    }
}
