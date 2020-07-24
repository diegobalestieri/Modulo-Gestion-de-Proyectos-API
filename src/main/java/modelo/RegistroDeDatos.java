package modelo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import excepciones.FechaInvalidaException;

import javax.persistence.Embeddable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistroDeDatos {

    private String nombre;
    private Date fechaDeInicio;
    private Date fechaDeFinalizacion;
    private String descripcion;

    public RegistroDeDatos() {
    }

    public String getNombre() { return nombre;}
    public String getDescripcion() { return descripcion;}


    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion;}

    public void asignarFechaDeInicio(String fechaDeInicio) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.fechaDeInicio = format.parse(fechaDeInicio);
    }
    public void asignarFechaDeFinalizacion(String fechaDeFinalizacion) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nuevaFechadeFinalizacion = format.parse(fechaDeFinalizacion);
        if (nuevaFechadeFinalizacion.compareTo(this.fechaDeInicio) < 0) {
            throw new FechaInvalidaException("La fecha de finalizacion debe ser posterior a la de inicio");
        }
        this.fechaDeFinalizacion  = nuevaFechadeFinalizacion;

    }

    public Date getFechaDeInicio() {
        return fechaDeInicio;
    }

    public Date getFechaDeFinalizacion() {
        return fechaDeFinalizacion;
    }

    public void setFechaDeInicio(Date fechaDeInicio) {
        this.fechaDeInicio = fechaDeInicio;
    }

    public void setFechaDeFinalizacion(Date fechaDeFinalizacion) {
        this.fechaDeFinalizacion = fechaDeFinalizacion;
    }

    @Override
    public boolean equals(Object o) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (this == o) return true;
        if (!(o instanceof RegistroDeDatos)) return false;
        RegistroDeDatos that = (RegistroDeDatos) o;
        return Objects.equals(nombre, that.nombre) &&
                format.format(fechaDeInicio).equals(format.format(that.fechaDeInicio)) &&
                format.format(fechaDeFinalizacion).equals(format.format(that.fechaDeFinalizacion)) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, fechaDeInicio, fechaDeFinalizacion, descripcion);
    }
}
