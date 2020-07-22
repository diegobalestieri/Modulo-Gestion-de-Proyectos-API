package excepciones;

import org.springframework.http.HttpStatus;

public class FechaInvalidaException extends RequestException {
    public FechaInvalidaException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
