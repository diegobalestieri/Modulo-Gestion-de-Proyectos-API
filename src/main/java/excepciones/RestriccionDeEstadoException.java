package excepciones;

import org.springframework.http.HttpStatus;

public class RestriccionDeEstadoException extends CustomException {

    public RestriccionDeEstadoException(String mensaje) {
        super(mensaje);
        this.responseStatus = HttpStatus.BAD_REQUEST;
    }
}
