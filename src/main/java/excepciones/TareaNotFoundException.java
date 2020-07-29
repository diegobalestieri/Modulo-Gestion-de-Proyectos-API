package excepciones;

import org.springframework.http.HttpStatus;

public class TareaNotFoundException extends CustomException {
    public TareaNotFoundException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
