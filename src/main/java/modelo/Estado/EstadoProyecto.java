package modelo.Estado;

public enum EstadoProyecto {

    NO_INICIADO("No iniciado"),
    ACTIVO("Activo"),
    SUSPENDIDO("Suspendido"),
    CANCELADO("Cancelado"),
    FINALIZADO("Finalizado");

    protected String nombre;

    EstadoProyecto(String nombre) { this.nombre = nombre; }

    public String getNombre() { return nombre;}

}


