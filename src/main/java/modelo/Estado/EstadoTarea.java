package modelo.Estado;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoTarea {

    NO_INICIADA("No iniciada"),
    EN_CURSO("En curso"),
    BLOQUEADA("Bloqueada"),
    FINALIZADA("Finalizada");

    protected String nombre;

    EstadoTarea(String nombre) { this.nombre = nombre; }

    @JsonValue
    public String toValue() {
        return nombre;
    }


    public String getNombre() { return nombre;}
}
