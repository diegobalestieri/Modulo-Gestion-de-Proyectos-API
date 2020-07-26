package controladores;

import com.fasterxml.jackson.databind.JsonMappingException;
import excepciones.FechaInvalidaException;
import modelo.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProyectoControllerAdvice {

    @ExceptionHandler(FechaInvalidaException.class)
    public ResponseEntity<Error> handleFechaInvalidaException(FechaInvalidaException e){
        return new ResponseEntity<Error>(new Error(e.getMessage(), e.getResponseStatus()), e.getResponseStatus());
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<Error> handleJsonMappingException(JsonMappingException e){
        System.out.print(e.getClass() + ": " + e.getMessage());
        String newMessage = e.getMessage().substring(0, e.getMessage().indexOf("\n at"));
        return new ResponseEntity<Error>(new Error(newMessage, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
}
