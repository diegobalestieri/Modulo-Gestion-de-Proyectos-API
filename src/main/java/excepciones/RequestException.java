package excepciones;

import org.springframework.http.HttpStatus;

public class RequestException extends RuntimeException{
    protected HttpStatus responseStatus;
    public RequestException(String mensaje){
        super(mensaje);
    }
    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

}
