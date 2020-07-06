package excepciones;

import org.springframework.http.HttpStatus;

public class RestriccionDeEstadoException extends RequestException {

    public RestriccionDeEstadoException(String mensaje) {
        super(mensaje);
        this.responseStatus = HttpStatus.BAD_REQUEST;
    }
}
