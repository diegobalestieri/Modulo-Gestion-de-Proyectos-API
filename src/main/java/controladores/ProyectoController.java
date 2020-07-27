package controladores;


import excepciones.*;
import modelo.Fase;
import modelo.Tarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import modelo.Proyecto;
import modelo.Error;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;
import servicio.ProyectoService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ProyectoController {
    @Autowired
    private ProyectoService servicio;


    @GetMapping("/proyectos")
    List<Proyecto> all(){
        return servicio.findAll();
    }

    @PostMapping("/proyectos")
    ResponseEntity<Proyecto> newProyecto(@RequestBody Proyecto proyecto){
        return new ResponseEntity<Proyecto>(servicio.saveNew(proyecto), HttpStatus.CREATED);
    }

    //Individual
    @GetMapping("/proyectos/{id}")
    ResponseEntity<?> obtenerProyecto(@PathVariable(value="id") Long id){
        try {
            return new ResponseEntity<Proyecto>(servicio.getOne(id), HttpStatus.OK);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }

    @PutMapping("/proyectos/{id}")
    ResponseEntity<Proyecto> guardarProyecto(@PathVariable(value="id") Long id, @RequestBody Proyecto proyecto){
        proyecto.setId(id);
        return new ResponseEntity<Proyecto>(servicio.save(proyecto), HttpStatus.OK);
    }

    @DeleteMapping("proyectos/{id}")
    ResponseEntity<?> borrarProyecto(@PathVariable(value="id") Long id){
        try{
            servicio.deleteById(id);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
        return new ResponseEntity<String>("Proyecto eliminado correctamente", HttpStatus.OK);
    }

    @PatchMapping("proyectos/{id}")
    ResponseEntity<?> actualizarProyecto(@PathVariable("id") Long id, @RequestBody Map<String, Object> parametros){
        try{
            Proyecto proyecto = servicio.getOne(id);
            servicio.update(proyecto, parametros);
            return new ResponseEntity<Proyecto>(proyecto, HttpStatus.OK);
        }catch (ProyectoNotFoundException | ParametrosInvalidosException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }

    @PostMapping(value = "proyectos/{id}/fases")
    ResponseEntity<?> crearFase(@PathVariable("id") Long proyectoId, @RequestBody Fase fase){
        try{
            ResponseEntity<Fase> fase_nueva = new ResponseEntity<Fase>(servicio.crearFase(proyectoId, fase), HttpStatus.CREATED);
            return fase_nueva;
        }catch (ProyectoNotFoundException | FechaInvalidaException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
        catch (ParseException e) {
           return new ResponseEntity<Error>(new Error(e.getMessage()), HttpStatus.valueOf(0)); // POLEMICO
       }
    }
    @GetMapping("proyectos/{id_proyecto}/fases/{id_fase}")
    ResponseEntity<?> crearFase(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId){
        try{
            return new ResponseEntity<Fase>(servicio.obtenerFase(proyectoId, faseId), HttpStatus.CREATED);
        }catch (ProyectoNotFoundException | FaseNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }

    @PutMapping("proyectos/{id_proyecto}/fases/{id_fase}")
    ResponseEntity<?> guardarFase(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId, @RequestBody Fase fase){
        try{
            return new ResponseEntity<Fase>(servicio.guardarFase(proyectoId, faseId, fase), HttpStatus.OK);
        }catch (ProyectoNotFoundException | FaseNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }
    @GetMapping("proyectos/{id_proyecto}/fases")
    ResponseEntity<?> obtenerFases(@PathVariable("id_proyecto") Long proyectoId){
        try{
            return new ResponseEntity<List<Fase>>(servicio.obtenerFases(proyectoId), HttpStatus.OK);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }
    @DeleteMapping("proyectos/{id_proyecto}/fases/{id_fase}")
    ResponseEntity<?> borrarFase(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId){
        try{
            servicio.borrarFase(proyectoId, faseId);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
        return new ResponseEntity<String>("Fase eliminada correctamente", HttpStatus.OK);
    }
//TAREAS
    @PostMapping(value = "proyectos/{id}/tareas")
    ResponseEntity<?> crearTarea(@PathVariable("id") Long proyectoId, @RequestBody Tarea tarea){
        try{
            return new ResponseEntity<Tarea>(servicio.crearTarea(proyectoId, tarea), HttpStatus.CREATED);
        }catch (ProyectoNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }

    @GetMapping("proyectos/{id_proyecto}/tareas")
    ResponseEntity<?> obtenerTareas(@PathVariable("id_proyecto") Long proyectoId){
        try{
            return new ResponseEntity<List<Tarea>>(servicio.obtenerTareas(proyectoId), HttpStatus.OK);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }

    }
    @GetMapping("proyectos/{id_proyecto}/tareas/{id_tarea}")
    ResponseEntity<?> obtenerTarea(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_tarea") Long tareaId){
        try{
            return new ResponseEntity<Tarea>(servicio.obtenerTarea(proyectoId, tareaId), HttpStatus.OK);
        } catch (ProyectoNotFoundException | TareaNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }
    @PutMapping("proyectos/{id_proyecto}/tareas/{id_tarea}")
    ResponseEntity<?> guardarTarea(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_tarea") Long tareaId, @RequestBody Tarea tarea){
        try{
            return new ResponseEntity<Tarea>(servicio.guardarTarea(proyectoId, tareaId,tarea), HttpStatus.OK);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }
    @DeleteMapping("proyectos/{id_proyecto}/tareas/{id_tarea}")
    ResponseEntity<?> borrarTarea(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_tarea") Long tareaId){
        try{
            servicio.borrarTarea(proyectoId, tareaId);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
        return new ResponseEntity<String>("Tarea eliminada correctamente", HttpStatus.OK);
    }

}
