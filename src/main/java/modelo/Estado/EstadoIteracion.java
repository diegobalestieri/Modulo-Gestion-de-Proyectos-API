package modelo.Estado;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoIteracion {

    CREADA("Creada"),
    ACTIVA("Activa"),
    FINALIZADA("Finalizada");

    protected String nombre;

    EstadoIteracion(String nombre) { this.nombre = nombre; }

    @JsonValue
    public String toValue() {
        return nombre;
    }


    public String getNombre() { return nombre;}
}
