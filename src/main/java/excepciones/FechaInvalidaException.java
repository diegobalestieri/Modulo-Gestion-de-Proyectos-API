package excepciones;

import org.springframework.http.HttpStatus;

public class FechaInvalidaException extends CustomException {
    public FechaInvalidaException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
