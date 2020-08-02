package DTOs;

import modelo.Estado.EstadoTarea;
import modelo.Tarea;

public class TareaResponsableDTO {
    private Long id;
    private String nombreTarea;
    private String nombreProyecto;
    private int duracionEstimada;
    private EstadoTarea estado;

    public TareaResponsableDTO(Tarea tarea){
        id = tarea.getId();
        nombreTarea = tarea.getNombre();
        duracionEstimada = tarea.getDuracionEstimada();
        estado = tarea.getEstado();
    }
    public TareaResponsableDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public int getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(int duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }
}
