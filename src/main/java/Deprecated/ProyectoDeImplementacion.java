package modelo;

import org.springframework.util.ReflectionUtils;
import persistencia.EntidadProyecto;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;

public class ProyectoDeImplementacion extends Proyecto {

    private String cliente;
    public ProyectoDeImplementacion() {
        super();
        tipoDeProyecto = "Implementación";
    }
    public ProyectoDeImplementacion(String nombre) {
        super(nombre);
        tipoDeProyecto = "Implementación";
    }

    public ProyectoDeImplementacion(EntidadProyecto proyecto) {
        super(proyecto);
        tipoDeProyecto = "Implementación";
        cliente = proyecto.getCliente();
    }

    public void asociarCliente(String nombreDeCliente) { cliente = nombreDeCliente;}

    public String getCliente() { return cliente; }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void actualizar(Map<String, Object> parametros) throws ParseException {
        for (Map.Entry<String, Object> entrada : parametros.entrySet()) {
            if (entrada.getKey().equals("cliente")) {
                this.setCliente((String) entrada.getValue());
            }
        }
        super.actualizar(parametros);
    }
    @Override
    public void ingresarDatos(EntidadProyecto entidadProyecto){
        entidadProyecto.setCliente(cliente);
    }
}


