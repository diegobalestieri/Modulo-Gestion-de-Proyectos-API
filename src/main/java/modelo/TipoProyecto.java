package modelo;


public enum TipoProyecto {

    IMPLEMENTACION("Implementación"),
    DESARROLLO("Desarrollo");

    private String nombre;

    TipoProyecto(String nombre){
            this.nombre = nombre;
    }
    public String getNombre() { return nombre;}
}
