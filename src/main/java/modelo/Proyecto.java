package modelo;

import com.fasterxml.jackson.annotation.JsonInclude;
import excepciones.*;
import modelo.Estado.EstadoProyecto;
import modelo.Estado.EstadoTarea;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Entity
@Table(name = "proyectos")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "proyecto_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoProyecto tipoDeProyecto;
    @Enumerated(EnumType.STRING)
    private EstadoProyecto estadoProyecto = EstadoProyecto.NO_INICIADO;
    private String liderDeProyecto;
    //Solo si es de implementacion
    private String cliente;
    //Solo si es de desarrollo
    private String producto;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Fase> fases = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Tarea> tareas = new ArrayList<>();

    @Embedded
    private RegistroDeDatos registroDeDatos = new RegistroDeDatos();

    public Proyecto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return registroDeDatos.getNombre();
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.equals(""))
            throw new ParametrosInvalidosException("No se puede crear un proyecto sin nombre");
        registroDeDatos.setNombre(nombre);
    }

    public TipoProyecto getTipoDeProyecto() {
        return tipoDeProyecto;
    }

    public void setTipoDeProyecto(String tipoDeProyecto) throws TipoDeProyectoInvalidoException {
        if (tipoDeProyecto.toLowerCase().equals("desarrollo")){
            this.tipoDeProyecto = TipoProyecto.DESARROLLO;
        } else if (tipoDeProyecto.toLowerCase().equals("implementación") || tipoDeProyecto.toLowerCase().equals("implementacion")){
            this.tipoDeProyecto = TipoProyecto.IMPLEMENTACION;
        } else{
            throw new TipoDeProyectoInvalidoException("El tipo " + tipoDeProyecto + " no es un tipo válido de proyecto");
        }
    }

    public String getDescripcion() {
        return registroDeDatos.getDescripcion();
    }

    public void setDescripcion(String descripcion) {
        registroDeDatos.setDescripcion(descripcion);
    }

    public Date getFechaDeInicio() {
        return registroDeDatos.getFechaDeInicio();
    }

    public void setFechaDeInicio(Date fechaDeInicio) {
        registroDeDatos.setFechaDeInicio(fechaDeInicio);
    }
    public void asignarFechaDeInicio(String fechaDeInicio) throws ParseException,AccionNoPermitidaException {
        registroDeDatos.asignarFechaDeInicio(fechaDeInicio);
    }

    public Date getFechaDeFinalizacion() {
        return registroDeDatos.getFechaDeFinalizacion();
    }

    public void setFechaDeFinalizacion(Date fechaDeFin) {
        registroDeDatos.setFechaDeFinalizacion(fechaDeFin);
    }

    public void asignarFechaDeFinalizacion(String fechaDeFinalizacion) throws ParseException,FechaInvalidaException {
        registroDeDatos.asignarFechaDeFinalizacion(fechaDeFinalizacion);
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

    public void actualizar(Map<String, Object> parametros) throws ParseException,AccionNoPermitidaException {
        //No se puede cambiar el tipo de proyecto
        validarTipoDeProyectoCorrecto(parametros.entrySet());
        for (Map.Entry<String, Object> entrada : parametros.entrySet()) {
            if (entrada.getKey().equals("nombre")) {
                this.setNombre((String) entrada.getValue());
            } else if (entrada.getKey().equals("descripcion")) {
                this.setDescripcion((String) entrada.getValue());
            } else if (entrada.getKey().equals("fechaDeInicio")) {
                Date nuevaFechaDeInicio = modificarFechaParaPatch((String) entrada.getValue());
                if (!estadoProyecto.equals(EstadoProyecto.NO_INICIADO))
                    registroDeDatos.validarNuevaFechaDeInicioDeProyecto(nuevaFechaDeInicio);
                this.setFechaDeInicio(nuevaFechaDeInicio);
            } else if (entrada.getKey().equals("fechaDeFinalizacion")) {
                Date nuevaFechaDeFinalizacion = modificarFechaParaPatch((String) entrada.getValue());
                this.setFechaDeFinalizacion(nuevaFechaDeFinalizacion);
            } else if (entrada.getKey().equals("estado")) {
                this.setEstado((String) entrada.getValue());
            } else if (entrada.getKey().equals("producto")){
                this.setProducto((String) entrada.getValue());
            } else if (tipoDeProyecto.equals(TipoProyecto.IMPLEMENTACION) && entrada.getKey().equals("cliente")){
                this.setCliente((String) entrada.getValue());
            } else if (entrada.getKey().equals("liderDeProyecto")){
                this.setLiderDeProyecto((String) entrada.getValue());
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
        Fase fase = obtenerFase(faseId);
        if (!fase.obtenerIteraciones().isEmpty())
            throw new AccionNoPermitidaException("No se puede eliminar una fase que cuenta con iteraciones cargadas");
        fases.remove(fase);
    }
    public void guardarFase(Fase fase) {
        for (int i = 0; i < fases.size(); ++i){
            Fase faseActual = fases.get(i);
            if (faseActual.getId().equals(fase.getId())){
                validarFechasDeFase(fase);
                rearmarFase(fase,faseActual);
                fases.set(i, fase);
                return;
            }
        }
        crearFase(fase);
    }
    public void validarFechasDeFase(Fase fase){
        if (fase.getFechaDeInicio() != null && this.getFechaDeInicio() != null) {
            if (fase.getFechaDeInicio().compareTo(this.getFechaDeInicio()) < 0)
                throw new FechaInvalidaException("La fecha de inicio de una fase no puede ser anterior a la del proyecto que la contiene");
        }
        if (fase.getFechaDeFinalizacion() != null && this.getFechaDeFinalizacion() != null) {
            if (fase.getFechaDeFinalizacion().compareTo(this.getFechaDeFinalizacion()) > 0)
                throw new FechaInvalidaException("La fecha de finalización de una fase no puede ser posterior a la del proyecto que la contiene");
        }
    }
    public boolean crearFase(Fase fase) {
        validarFechasDeFase(fase);
        fases.add(fase);
        return true;
    }

    public void crearTarea(Tarea tarea) {
        if (tarea.getFechaDeInicio() == null){
            tarea.setFechaDeInicio(new Date());
        }
        tareas.add(tarea);
    }

    public List<Tarea> obtenerTareas() {
        return tareas;
    }

    public Tarea obtenerTarea(Long tareaId) {
        for (Tarea tarea : tareas){
            if (tarea.getId().equals(tareaId)){
                return tarea;
            }
        }
        throw new TareaNotFoundException("La tarea no fue encontrada");
    }

    public void guardarTarea(Tarea tarea) {
        for (int i = 0; i < tareas.size(); ++i){
            Tarea tareaActual = tareas.get(i);
            if (tareaActual.getId().equals(tarea.getId())){
                tareas.set(i, tarea);
                return;
            }
        }
        crearTarea(tarea);
    }
    public void borrarTarea(Long tareaId) {
        for (Tarea tarea : tareas) {
            if (tarea.getId().equals(tareaId)) {
                tareas.remove(tarea);
                return;
            }
        }
        throw new TareaNotFoundException("La tarea no fue encontrada");
    }

    public List<Tarea> obtenerTareasSinIteracion() {
        List<Tarea> nuevaLista = new ArrayList();
        for (Tarea tarea : tareas) {
            if (tarea.getIteracion() == 0 && !tarea.getEstado().equals(EstadoTarea.FINALIZADA))
                nuevaLista.add(tarea);
        }
        return nuevaLista;
    }

    public List<Tarea> obtenerTareasDelResponsable(String responsableId) {
        List<Tarea> listaADevolver = new ArrayList();
        for (Tarea tarea : tareas) {
            if (responsableId.equals(tarea.getResponsable()))
                listaADevolver.add(tarea);
        }
        return listaADevolver;
    }

    public void rearmarFase(Fase faseAGuardar, Fase faseAnterior) {
        faseAGuardar.setIteraciones(faseAnterior.obtenerIteraciones());
        faseAGuardar.setCantidadDeIteraciones(faseAnterior.getCantidadDeIteraciones());
    }

    public String getLiderDeProyecto() {
        return liderDeProyecto;
    }

    public void setLiderDeProyecto(String liderDeProyecto) {
        this.liderDeProyecto = liderDeProyecto;
    }

    public void validarTipoDeProyectoCorrecto(Set<Map.Entry<String,Object>> entrySet) {
        for (Map.Entry<String, Object> entrada : entrySet) {
            if (entrada.getKey().equals("tipoDeProyecto") && !entrada.getValue().equals(tipoDeProyecto.getNombre()))
                throw new AccionNoPermitidaException("No se puede cambiar el tipo de proyecto");
        }
    }

    public Date modificarFechaParaPatch(String fechaOriginal) throws ParseException {
        DateFormat format_1 = new SimpleDateFormat("yyyy-MM-dd");
        format_1.setTimeZone(TimeZone.getTimeZone("Argentina"));
        Date nuevaFecha = format_1.parse(fechaOriginal);
        return nuevaFecha;
    }

}
