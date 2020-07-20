package servicio;

import excepciones.ParametrosInvalidosException;
import excepciones.ProyectoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import modelo.Fase;
import persistencia.FasesRepository;
import modelo.Proyecto;
import persistencia.ProyectosRepository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
public class ProyectoService {
    @Autowired
    private ProyectosRepository proyectosRepository;

    @Autowired
    private FasesRepository fasesRepository;

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public List<Proyecto> findAll(){
        return proyectosRepository.findAll();
    }
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Proyecto save(Proyecto proyecto){
        return proyectosRepository.save(proyecto);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Proyecto saveNew(Proyecto proyecto){
        proyecto.setId(null);
        return proyectosRepository.save(proyecto);
    }
    public void delete(Proyecto proyecto){
        proyectosRepository.delete(proyecto);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor=Exception.class)
    public Proyecto getOne(long id) {
        if (!proyectosRepository.existsById(id)){
            throw new ProyectoNotFoundException("El proyecto no fue encontrado");
        }
        return proyectosRepository.getOne(id);
    }

    public void deleteById(long id) {
        proyectosRepository.deleteById(id);
    }

    public void deleteAll() {
        proyectosRepository.deleteAll();
    }

    public void update(Proyecto proyecto, Map<String, Object> parametros) {
        try{
            proyecto.actualizar(parametros);
        } catch (ParseException e){
            throw new ParametrosInvalidosException(e.getMessage());
        }
        proyectosRepository.save(proyecto);
    }

    public Fase crearFase(Long proyectoId, Fase fase) {
        Proyecto proyecto = getOne(proyectoId);
        proyecto.crearFase(fase);
        Proyecto entidadProyecto = proyectosRepository.save(proyecto);
        List <Fase> fases = entidadProyecto.getFases();
        return fases.get(fases.size()-1);
    }
/*
ProyectoService
public void crearFase(Long proyectoId, Fase fase){
    Proyecto proyecto = getOne(proyectoId);
    proyecto.crearFase(fase);
    proyectosRepository.update()
    repo.update(p);
}*/
    public Fase obtenerFase(Long proyectoId, Long faseId) {
        Proyecto proyecto = getOne(proyectoId);
        return proyecto.obtenerFase(faseId);
    }

   public List<Fase> obtenerFases(Long proyectoId) {
        Proyecto proyecto = getOne(proyectoId);
        return proyecto.obtenerFases();
    }
 /*public List<Fase> obtenerFases(Long proyectoId) {
     List<EntidadFase> entidades = fasesRepository.findByProyecto(proyectosRepository.getOne(proyectoId));
     List<Fase> lista = new ArrayList<Fase>();
     for (int i = 0; i < entidades.size(); i++){
         lista.add(new Fase(entidades.get(i)));
     }
     return lista;
 }*/

    public void borrarFase(Long proyectoId, Long faseId) {
        Proyecto proyecto = getOne(proyectoId);
        proyecto.borrarFase(faseId);
        save(proyecto);
        fasesRepository.deleteById(faseId);
    }

    public Fase guardarFase(Long proyectoId, Long faseId,Fase fase) {
        Proyecto proyecto = getOne(proyectoId);
        fase.setId(faseId);
        proyecto.guardarFase(fase);
        Proyecto entidadProyecto = proyectosRepository.save(proyecto);
        List <Fase> fases = entidadProyecto.getFases();
        return fases.get(fases.size()-1);
    }
}
