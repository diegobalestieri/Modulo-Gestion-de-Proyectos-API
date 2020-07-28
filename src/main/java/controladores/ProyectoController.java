package controladores;


import excepciones.*;
import modelo.*;
import modelo.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("proyectos/{id_proyecto}/fases/{id_fase}/iteraciones")
    ResponseEntity<?> crearIteracion(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId, @RequestBody Iteracion iteracion) {
        try{
            return new ResponseEntity<Iteracion>(servicio.crearIteracion(proyectoId, faseId,iteracion), HttpStatus.CREATED);
        }catch (ProyectoNotFoundException | FaseNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }

    @GetMapping("proyectos/{id_proyecto}/fases/{id_fase}/iteraciones")
    ResponseEntity<?> obtenerIteraciones(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId) {
        try{
            return new ResponseEntity<List<Iteracion>>(servicio.obtenerIteraciones(proyectoId, faseId), HttpStatus.OK);
        }catch (ProyectoNotFoundException | FaseNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }

    @PutMapping("proyectos/{id_proyecto}/fases/{id_fase}/iteraciones/{id_iteracion}")
    ResponseEntity<?> guardarIteracion(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId,
                                       @PathVariable("id_iteracion") Long iteracionId,@RequestBody Iteracion iteracion){
        try{
            return new ResponseEntity<Iteracion>(servicio.guardarIteracion(proyectoId, faseId,iteracionId,iteracion), HttpStatus.OK);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }
    @GetMapping("proyectos/{id_proyecto}/fases/{id_fase}/iteraciones/{id_iteracion}")
    ResponseEntity<?> obtenerIteracion(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId,
                                       @PathVariable("id_iteracion") Long iteracionId){
        try{
            return new ResponseEntity<Iteracion>(servicio.obtenerIteracion(proyectoId, faseId,iteracionId), HttpStatus.OK);
        } catch (ProyectoNotFoundException | TareaNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }
    @DeleteMapping("proyectos/{id_proyecto}/fases/{id_fase}/iteraciones/{id_iteracion}")
    ResponseEntity<?> borrarIteracion(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId,
                                      @PathVariable("id_iteracion") Long iteracionId){
        try{
            servicio.borrarIteracion(proyectoId, faseId,iteracionId);
        } catch (ProyectoNotFoundException | AccionNoPermitidaException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
        return new ResponseEntity<String>("Iteracion eliminada correctamente", HttpStatus.OK);
    }
    @GetMapping("proyectos/{id_proyecto}/fases/{id_fase}/iteraciones/{id_iteracion}/tareas")
    ResponseEntity<?> obtenerTareasDeIteracion(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId,
                                       @PathVariable("id_iteracion") Long iteracionId){
        try{
            return new ResponseEntity<List<Tarea>>(servicio.obtenerTareasDeIteracion(proyectoId, faseId,iteracionId), HttpStatus.OK);
        } catch (ProyectoNotFoundException | TareaNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
    }
    @PostMapping("proyectos/{id_proyecto}/fases/{id_fase}/iteraciones/{id_iteracion}/tareas")
    ResponseEntity<?> agregarTareaAIteracion(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId,
                                             @PathVariable("id_iteracion") Long iteracionId, @RequestParam long id_tarea) {
        try{
            servicio.agregarTareaAIteracion(proyectoId, faseId,iteracionId,id_tarea);
        }catch (ProyectoNotFoundException | FaseNotFoundException | TareaNotFoundException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
        return new ResponseEntity<String>("Tarea cargada a la iteracion correctamente", HttpStatus.OK);
    }
    @DeleteMapping("proyectos/{id_proyecto}/fases/{id_fase}/iteraciones/{id_iteracion}/tareas/{id_tarea}")
    ResponseEntity<?> borrarTareaDeIteracion(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId,
                                      @PathVariable("id_iteracion") Long iteracionId,@PathVariable("id_tarea") Long tareaId){
        try{
            servicio.borrarTareaDeIteracion(proyectoId, faseId,iteracionId,tareaId);
        } catch (ProyectoNotFoundException | AccionNoPermitidaException e){
            return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
        }
        return new ResponseEntity<String>("Tarea eliminada de la iteracion correctamente", HttpStatus.OK);
    }




}
