package excepciones;

import org.springframework.http.HttpStatus;

public class ParametrosInvalidosException extends RequestException {

    public ParametrosInvalidosException(String mensaje) {
        super(mensaje);
        this.responseStatus = HttpStatus.BAD_REQUEST;
    }
}
