package modelo;

import excepciones.AccionNoPermitidaException;
import excepciones.FaseNotFoundException;
import excepciones.RestriccionDeEstadoException;
import excepciones.TipoDeProyectoInvalido;
import modelo.Estado.EstadoProyecto;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "proyecto_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoProyecto tipoDeProyecto;
    private String nombre;
    private String descripcion;


    @Enumerated(EnumType.STRING)
    private EstadoProyecto estadoProyecto = EstadoProyecto.NO_INICIADO;

    private Date fechaDeInicio;
    private Date fechaDeFin;
    //Solo si es de implementacion
    private String cliente;
    //Solo si es de desarrollo
    private String producto;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Fase> fases = new ArrayList<>();


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Tarea> tareas = new ArrayList<>();

    public Proyecto() {}

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

    public TipoProyecto getTipoDeProyecto() {
        return tipoDeProyecto;
    }

    public void setTipoDeProyecto(String tipoDeProyecto) throws TipoDeProyectoInvalido {
        if (tipoDeProyecto.toLowerCase().equals("desarrollo")){
            this.tipoDeProyecto = TipoProyecto.DESARROLLO;
        } else if (tipoDeProyecto.toLowerCase().equals("implementación") || tipoDeProyecto.toLowerCase().equals("implementacion")){
            this.tipoDeProyecto = TipoProyecto.IMPLEMENTACION;
        } else{
            throw new TipoDeProyectoInvalido("El tipo " + tipoDeProyecto + " no es un tipo válido de proyecto");
        }
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
        if (!estadoProyecto.getNombre().equals("No iniciado")) {
            throw new RestriccionDeEstadoException("No se puede cambiar la fecha de inicio de un proyecto iniciado");
        }
        this.fechaDeInicio = fechaDeInicio;
    }
    public void setFechaDeInicio(String fechaDeInicio) throws ParseException {
        if (!estadoProyecto.getNombre().equals("No iniciado")) {
            throw new RestriccionDeEstadoException("No se puede cambiar la fecha de inicio de un proyecto iniciado");
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.fechaDeInicio = format.parse(fechaDeInicio);
    }

    public Date getFechaDeFin() {
        return fechaDeFin;
    }

    public void setFechaDeFin(Date fechaDeFin) {
        this.fechaDeFin = fechaDeFin;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        if (tipoDeProyecto == TipoProyecto.IMPLEMENTACION){
            this.cliente = cliente;
        }
    }
    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public List<Fase> getFases() {
        return fases;
    }

    public void setFases(List<Fase> fases) {
        this.fases = fases;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        if (tipoDeProyecto == TipoProyecto.DESARROLLO){
            this.producto = producto;
        }
    }
    public EstadoProyecto getEstado() {
        return estadoProyecto;
    }

    public boolean setEstado(String nombreDeEstado) {
        if (this.estadoProyecto != EstadoProyecto.NO_INICIADO && nombreDeEstado.equals("No iniciado")) {
            throw new AccionNoPermitidaException("No se puede volver al estado No iniciado");
        }
        switch (nombreDeEstado) {
            case "No iniciado": this.estadoProyecto = EstadoProyecto.NO_INICIADO;
                break;
            case "Activo": this.estadoProyecto = EstadoProyecto.ACTIVO;
                break;
            case "Suspendido": this.estadoProyecto = EstadoProyecto.SUSPENDIDO;
                break;
            case "Cancelado": this.estadoProyecto = EstadoProyecto.CANCELADO;
                break;
            case "Finalizado": this.estadoProyecto = EstadoProyecto.FINALIZADO;
                break;
        }
        return true;
    }
    public boolean setEstado(EstadoProyecto nuevoEstado) {
        if ((this.estadoProyecto != EstadoProyecto.NO_INICIADO) && (nuevoEstado == EstadoProyecto.NO_INICIADO)) {
            throw new AccionNoPermitidaException("No se puede volver al estado No iniciado");
        }
        this.estadoProyecto = nuevoEstado;
        return true;
    }

    public void actualizar(Map<String, Object> parametros) throws ParseException {
        //No se puede cambiar el tipo de proyecto
        for (Map.Entry<String, Object> entrada : parametros.entrySet()) {
            if (entrada.getKey().equals("nombre")) {
                this.setNombre((String) entrada.getValue());
            } else if (entrada.getKey().equals("descripcion")) {
                this.setDescripcion((String) entrada.getValue());
           /* } else if (entrada.getKey().equals("fechaDeInicio")) {
                this.setFechaDeInicio((String) entrada.getValue());
            } else if (entrada.getKey().equals("fechaDeFinalizacion")) {
                this.setFechaDeFin((String) entrada.getValue());
            */
            } else if (entrada.getKey().equals("estado")) {
                this.setEstado((String) entrada.getValue());
            } else if (tipoDeProyecto.equals("Desarrollo") && entrada.getKey().equals("producto")){
                this.setProducto((String) entrada.getValue());
            } else if (tipoDeProyecto.equals("Implementación") && entrada.getKey().equals("cliente")){
                this.setCliente((String) entrada.getValue());
            }

        }
    }
    public Fase obtenerFase(Long faseId){
        for (Fase fase : fases) {
            if (fase.getId().equals(faseId)) {
                return fase;
            }
        }
        throw new FaseNotFoundException("La fase no fue encontrada");
    }
    public List<Fase> obtenerFases() {
        return fases;
    }
    public void borrarFase(Long faseId) {
        for (Fase fase : fases) {
            if (fase.getId().equals(faseId)) {
                fases.remove(fase);
                return;
            }
        }
        throw new FaseNotFoundException("La fase no fue encontrada");
    }
    public void guardarFase(Fase fase) {
        for (int i = 0; i < fases.size(); ++i){
            Fase faseActual = fases.get(i);
            if (faseActual.getId().equals(fase.getId())){
                fase.setId(faseActual.getId());
                fases.set(i, fase);
                return;
            }
        }
        crearFase(fase);
    }
    public boolean crearFase(Fase fase) {
        fases.add(fase);
        return true;
    }

    public void crearTarea(Tarea tarea) {
        tareas.add(tarea);
    }

    public List<Tarea> obtenerTareas() {
        return tareas;
    }
}
