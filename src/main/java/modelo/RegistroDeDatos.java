package modelo;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Embeddable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Embeddable
public class RegistroDeDatos {

    private String nombre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaDeInicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaDeFinalizacion;
    private String descripcion;

    public RegistroDeDatos() {

    }

    public String getNombre() { return nombre;}
    public String getDescripcion() { return descripcion;}
    public Date getFechaDeInicio() { return fechaDeInicio;}
    public Date getFechaDeFinalizacion() { return fechaDeFinalizacion;}

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion;}

    public void setFechaDeInicio(String fechaDeInicio) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.fechaDeInicio = format.parse(fechaDeInicio);
    }

    public void setFechaDeFinalizacion(String fechaDeFinalizacion) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.fechaDeFinalizacion = format.parse(fechaDeFinalizacion);
    }


    public void setFechaDeInicio(Date fechaDeInicio) {
        this.fechaDeInicio = fechaDeInicio;
    }

    public void setFechaDeFinalizacion(Date fechaDeFin) {
        this.fechaDeFinalizacion = fechaDeFin;
    }
}
