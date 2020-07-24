package modelo;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "fases")
public class Fase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fase_id")
    private Long id;
    private String nombre;
    private String descripcion;
    private Date fechaDeInicio;
    private Date fechaDeFin;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Iteracion> iteraciones = new ArrayList<>();

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

    public boolean agregarIteracion(Iteracion iteracion) {
        return this.iteraciones.add(iteracion);
    }

    public List<Iteracion> getIteraciones() { return this.iteraciones;  }
}
