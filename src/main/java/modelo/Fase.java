package modelo;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "fases")
public class Fase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private String descripcion;
    private String estado;
    private Date fechaDeInicio;
    private Date fechaDeFin;

    public Fase(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaDeInicio() {
        return fechaDeInicio;
    }

    public void setFechaDeInicio(Date fechaDeInicio) {
        this.fechaDeInicio = fechaDeInicio;
    }

    public Date getFechaDeFinalizacion() {
        return fechaDeFin;
    }

    public void setFechaDeFinalizacion(Date fechaDeFin) {
        this.fechaDeFin = fechaDeFin;
    }

    public void setFechaDeInicio(String fecha_de_inicio) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.fechaDeInicio = format.parse(fecha_de_inicio);
    }
    public void setFechaDeFinalizacion(String fecha_de_fin) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.fechaDeFin = format.parse(fecha_de_fin);
    }
}
