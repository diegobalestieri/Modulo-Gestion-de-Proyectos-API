package modelo.Estado;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoTarea {

    TO_DO("Por hacer"),
    IN_PROGRESS("En progreso"),
    BLOCKED("Bloqueada"),
    DONE("Finalizada");

    protected String nombre;

    EstadoTarea(String nombre) { this.nombre = nombre; }

    @JsonValue
    public String toValue() {
        return nombre;
    }


    public String getNombre() { return nombre;}
}
