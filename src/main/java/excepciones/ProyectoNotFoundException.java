package excepciones;

import org.springframework.http.HttpStatus;

public class ProyectoNotFoundException extends CustomException {
    public ProyectoNotFoundException(String mensaje) {
        super(mensaje);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
