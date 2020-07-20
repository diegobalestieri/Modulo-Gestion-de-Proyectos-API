package modelo.Estado;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoProyecto {

    NO_INICIADO("No iniciado"),
    ACTIVO("Activo"),
    SUSPENDIDO("Suspendido"),
    CANCELADO("Cancelado"),
    FINALIZADO("Finalizado");

    protected String nombre;

    EstadoProyecto(String nombre) { this.nombre = nombre; }

    @JsonValue
    public String toValue() {
       return nombre;
    }

    public String getNombre() { return nombre;}

}


