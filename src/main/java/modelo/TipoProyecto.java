package modelo;

import excepciones.TipoDeProyectoInvalido;

public enum TipoProyecto {

    IMPLEMENTACION("Implementación"),
    DESARROLLO("Desarrollo");

    protected String nombre;

    TipoProyecto(String nombre){
            this.nombre = nombre;
    }
    public String getNombre() { return nombre;}
}
