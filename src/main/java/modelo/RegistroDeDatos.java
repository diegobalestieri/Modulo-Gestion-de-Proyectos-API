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
        if (fechaDeInicio == null || fechaDeInicio.equals("")) {return;}
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nuevaFechaDeInicio = format.parse(fechaDeInicio);
        setFechaDeInicio(nuevaFechaDeInicio);
    }
    public void asignarFechaDeFinalizacion(String fechaDeFinalizacion) throws ParseException,FechaInvalidaException {
        if (fechaDeFinalizacion == null || fechaDeFinalizacion.equals("")) {return;}
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nuevaFechadeFinalizacion = format.parse(fechaDeFinalizacion);
        setFechaDeFinalizacion(nuevaFechadeFinalizacion);
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

    public void setFechaDeFinalizacion(Date fechaDeFinalizacion) throws FechaInvalidaException {
        if (this.fechaDeInicio != null && fechaDeFinalizacion.compareTo(this.fechaDeInicio) < 0) {
            throw new FechaInvalidaException("La fecha de finalizacion debe ser posterior a la de inicio");
        }
        this.fechaDeFinalizacion = fechaDeFinalizacion;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof RegistroDeDatos)) return false;
        RegistroDeDatos that = (RegistroDeDatos) o;
        return Objects.equals(nombre, that.nombre) &&
                lasFechasSonIguales(fechaDeInicio,that.fechaDeInicio) &&
                lasFechasSonIguales(fechaDeFinalizacion,that.fechaDeFinalizacion) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, fechaDeInicio, fechaDeFinalizacion, descripcion);
    }

    public boolean lasFechasSonIguales(Date fecha_1,Date fecha_2) {
        if (fecha_1 == null && fecha_2 == null) { return true; }
        if (fecha_1 == null || fecha_2 == null) { return false; }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return (format.format(fecha_1).equals(format.format(fecha_2)));
    }
}
