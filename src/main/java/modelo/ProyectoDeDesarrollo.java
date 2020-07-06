package modelo;

import org.springframework.util.ReflectionUtils;
import persistencia.EntidadProyecto;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Map;


public class ProyectoDeDesarrollo extends Proyecto {

    private String producto;
    public  ProyectoDeDesarrollo() {
        super();
        this.tipoDeProyecto = "Desarrollo";
    }
    public ProyectoDeDesarrollo(EntidadProyecto proyecto) {
        super(proyecto);
        tipoDeProyecto = "Desarrollo";
        producto = proyecto.getProducto();
    }

    public ProyectoDeDesarrollo(String nombre) {
        super(nombre);
        tipoDeProyecto = "Desarrollo";
    }

    public void actualizar(Map<String, Object> parametros) throws ParseException {
        for (Map.Entry<String, Object> entrada : parametros.entrySet()) {
            if (entrada.getKey().equals("producto")) {
                this.setProducto((String) entrada.getValue());
            }
        }
        super.actualizar(parametros);
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }
    @Override
    public void ingresarDatos(EntidadProyecto entidadProyecto){
        entidadProyecto.setProducto(producto);
    }


}
