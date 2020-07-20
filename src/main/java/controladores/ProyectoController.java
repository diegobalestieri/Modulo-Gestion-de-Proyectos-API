package controladores;


import excepciones.FaseNotFoundException;
import excepciones.ParametrosInvalidosException;
import excepciones.ProyectoNotFoundException;
import modelo.Fase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import modelo.Proyecto;
import servicio.ProyectoService;

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
            return new ResponseEntity<String>(e.getMessage(), e.getResponseStatus());
        }
    }

    @PutMapping("/proyectos/{id}")
    ResponseEntity<Proyecto> guardarProyecto(@PathVariable(value="id") Long id, @RequestBody Proyecto proyecto){
        proyecto.setId(id);
        return new ResponseEntity<Proyecto>(servicio.save(proyecto), HttpStatus.OK);
    }

    @DeleteMapping("proyectos/{id}")
    ResponseEntity<String> borrarProyecto(@PathVariable(value="id") Long id){
        try{
            servicio.deleteById(id);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), e.getResponseStatus());
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
            return new ResponseEntity<String>(e.getMessage(), e.getResponseStatus());
        }
    }

    @PostMapping(value = "proyectos/{id}/fases")
    ResponseEntity<?> crearFase(@PathVariable("id") Long proyectoId, @RequestBody Fase fase){
        try{
            return new ResponseEntity<Fase>(servicio.crearFase(proyectoId, fase), HttpStatus.CREATED);
        }catch (ProyectoNotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), e.getResponseStatus());
        }
    }
    @GetMapping("proyectos/{id_proyecto}/fases/{id_fase}")
    ResponseEntity<?> crearFase(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId){
        try{
            return new ResponseEntity<Fase>(servicio.obtenerFase(proyectoId, faseId), HttpStatus.CREATED);
        }catch (FaseNotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), e.getResponseStatus());
        }
    }

    @PutMapping("proyectos/{id_proyecto}/fases/{id_fase}")
    ResponseEntity<?> guardarFase(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId, @RequestBody Fase fase){
        try{
            return new ResponseEntity<Fase>(servicio.guardarFase(proyectoId, faseId, fase), HttpStatus.OK);
        }catch (ProyectoNotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), e.getResponseStatus());
        }
    }
    @GetMapping("proyectos/{id_proyecto}/fases")
    ResponseEntity<?> crearFase(@PathVariable("id_proyecto") Long proyectoId){
        try{
            return new ResponseEntity<List<Fase>>(servicio.obtenerFases(proyectoId), HttpStatus.OK);
        } catch (FaseNotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), e.getResponseStatus());
        }
    }
    @DeleteMapping("proyectos/{id_proyecto}/fases/{id_fase}")
    ResponseEntity<String> borrarFase(@PathVariable("id_proyecto") Long proyectoId, @PathVariable("id_fase") Long faseId){
        try{
            servicio.borrarFase(proyectoId, faseId);
        } catch (ProyectoNotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), e.getResponseStatus());
        }
        return new ResponseEntity<String>("Proyecto eliminado correctamente", HttpStatus.OK);
    }
}
