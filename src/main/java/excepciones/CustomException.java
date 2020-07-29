package excepciones;

import org.springframework.http.HttpStatus;

public class CustomException extends RequestException {
    public CustomException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
