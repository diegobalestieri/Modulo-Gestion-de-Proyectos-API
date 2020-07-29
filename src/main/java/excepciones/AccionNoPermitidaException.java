package excepciones;

import org.springframework.http.HttpStatus;

public class AccionNoPermitidaException extends CustomException {
    public AccionNoPermitidaException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
