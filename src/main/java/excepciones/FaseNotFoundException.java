package excepciones;

import org.springframework.http.HttpStatus;

public class FaseNotFoundException extends CustomException {
    public FaseNotFoundException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
