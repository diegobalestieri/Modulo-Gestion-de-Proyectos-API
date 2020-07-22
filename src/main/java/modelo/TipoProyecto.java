package modelo;


import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoProyecto {

    IMPLEMENTACION("Implementación"),
    DESARROLLO("Desarrollo");

    private String nombre;

    TipoProyecto(String nombre){
            this.nombre = nombre;
    }

    @JsonValue
    public String toValue() {
        return nombre;
    }

    public String getNombre() { return nombre;}
}
