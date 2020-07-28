package excepciones;

import org.springframework.http.HttpStatus;

public class IteracionNotFoundException extends RequestException {
    public IteracionNotFoundException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
