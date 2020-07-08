package excepciones;

import org.springframework.http.HttpStatus;

public class AccionNoPermitidaException extends RequestException {
    public AccionNoPermitidaException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
