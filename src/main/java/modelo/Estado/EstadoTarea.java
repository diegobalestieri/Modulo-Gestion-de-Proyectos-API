package modelo.Estado;

public enum EstadoTarea {

    TO_DO("Por hacer"),
    IN_PROGRESS("En progreso"),
    DONE("Finalizada");

    protected String nombre;

    EstadoTarea(String nombre) { this.nombre = nombre; }

    public String getNombre() { return nombre;}
}
