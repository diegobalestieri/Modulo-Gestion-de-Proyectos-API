package excepciones;

import org.springframework.http.HttpStatus;

public class FaseNotFoundException extends RequestException {
    public FaseNotFoundException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
