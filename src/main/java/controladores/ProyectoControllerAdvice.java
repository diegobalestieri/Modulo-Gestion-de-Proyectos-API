package controladores;

import com.fasterxml.jackson.databind.JsonMappingException;
import excepciones.CustomException;
import excepciones.FechaInvalidaException;
import modelo.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProyectoControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Error> handleFechaInvalidaException(CustomException e){
        return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<Error> handleJsonMappingException(JsonMappingException e){
        // se puede sacar el nombre de la excepcion que origina la JsonMapping Exception con
        // e.getCause().getClass().getName() -> Ej: excepciones.FechaInvalidaException
        //System.out.print(e.getClass() + ": " + e.getMessage());
        //if (e.getCause().getClass().getName().contains("ParseError")) {
           // return new ResponseEntity<Error>(new Error("Error de parseo", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        //}
        String newMessage = e.getMessage().substring(0, e.getMessage().indexOf("\n at"));
        return new ResponseEntity<Error>(new Error(newMessage, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
}
