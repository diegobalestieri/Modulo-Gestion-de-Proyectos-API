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
    private Map<String,String> diccionario_nombre_tipo_dt = new HashMap();
    private Map<String,String> diccionario_nombre_tipo_proyecto = new HashMap();
    private Proyecto proyecto;

    @Then("se me devuelven los tipos correctos para cada proyecto")
    public void losTiposDeProyectoSonCorrectos(DataTable dt) {
        List<Map<String, String>> lista = dt.asMaps();
        int diferencias_encontradas = 0;
        for (Map<String, String> fila : lista) {
            Proyecto proyecto = proyectoService.getOne(diccionario_nombre_id.get(fila.get("nombre")));
            assertEquals(fila.get("tipo"),proyecto.getTipoDeProyecto());
        }

    }

    @Given("un listado con proyectos cargados de distinto tipo")
    public void unListadoConProyectosCargadosDeDistintoTipo(DataTable dt) {
        List<Map<String,String>> listaDeMapas = dt.asMaps();
        Proyecto proyecto;
        Proyecto proyecto_guardado;
        for (Map<String,String> fila: listaDeMapas) {
            if (fila.get("tipo").equals("Implementación")) {
                proyecto = new ProyectoDeImplementacion(fila.get("nombre"));
            }
            else {
                proyecto = new ProyectoDeDesarrollo(fila.get("nombre"));
            }
            proyecto_guardado = proyectoService.save(proyecto);
            diccionario_nombre_id.put(fila.get("nombre"),proyecto_guardado.getId());
            diccionario_nombre_tipo_dt.put(fila.get("nombre"),fila.get("tipo"));
        }
    }

    @When("pregunto el tipo de cada proyecto")
    public void preguntoElTipoDeCadaProyecto() {
        for (Proyecto proyecto: proyectoService.findAll()) {
            String nombre = proyecto.getNombre();
            String tipo = proyecto.getTipoDeProyecto();
            diccionario_nombre_tipo_proyecto.put(nombre,tipo);
        }
    }

    @Then("se me devuelven los tipos correctos")
    public void seMeDevuelvenLosTiposCorrectos() {
        for (String nombre: diccionario_nombre_tipo_proyecto.keySet()) {
            assertEquals(diccionario_nombre_tipo_dt.get(nombre),diccionario_nombre_tipo_proyecto.get(nombre));
        }

    }

    @When("agrego al cliente {string} a un proyecto de Implementacion")
    public void agregoUnClienteAUnProyectoDeImplementacion(String nombreCliente) {
        ProyectoDeImplementacion proyecto = new ProyectoDeImplementacion("Sistema MS");
        this.proyecto = proyecto;
        proyecto.setCliente(nombreCliente);
    }

    @Then("el cliente {string} se agrego al proyecto correctamente")
    public void elClienteSeAgregoAlProyectoCorrectamente(String nombreCliente) {
        assertEquals(nombreCliente,((ProyectoDeImplementacion) proyecto).getCliente());
    }

    @When("agrego al producto {string} a un proyecto de Desarrollo")
    public void agregoAlProductoAUnProyectoDeImplementacion(String nombreDeProducto) {
        this.proyecto = new ProyectoDeDesarrollo("ERP v 1.4");
        ((ProyectoDeDesarrollo) this.proyecto).setProducto("ERP");
    }

    @Then("el producto {string} se agrego al proyecto correctamente")
    public void elProductoSeAgregoAlProyectoCorrectamente(String nombreDeProducto) {
        assertEquals(nombreDeProducto,((ProyectoDeDesarrollo) proyecto).getProducto());
    }
}
