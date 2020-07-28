package modelo.Estado;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoFase {

    CREADA("Creada"),
    ACTIVA("Activa"),
    FINALIZADA("Finalizada");

    protected String nombre;

    EstadoFase(String nombre) { this.nombre = nombre; }

    @JsonValue
    public String toValue() {
        return nombre;
    }


    public String getNombre() { return nombre;}
}
