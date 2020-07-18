package TestsProyecto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import modelo.Proyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefProyectoAPI extends SpringTest{

    List<String> ids = new ArrayList<>();
    @Before
    public void setup() {
        mapper.setDateFormat(this.df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    @When("tengo el listado de proyectos")
    public void tengoUnListadoDeProyectos() {
    }


    @When("Creo un proyecto con nombre, descripcion y tipo")
    public void creoUnProyectoConNombreDescripcionYTipo(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        Proyecto proyecto;
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).get("tipo").equals("Desarrollo")){
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
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(requestJson)).andReturn();
                    //.andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value(list.get(i).get("descripcion")));
            System.out.print("Respuesta: " + requestResult.getResponse().getContentAsString());
            String response = requestResult.getResponse().getContentAsString();
            this.ids.add(obtenerId(response));
        }
    }

    @Then("el proyecto se crea con los datos correctos")
    public void elProyectoSeCreaConLosDatosCorrectos(DataTable dt) throws Exception {
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        for (int i = 0; i < list.size(); i++){
            this.mockMvc.perform(get("/proyectos")
                    .param("id", ids.get(i)))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value(list.get(i).get("nombre")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].descripcion").value(list.get(i).get("descripcion")));
        }

    }
}
