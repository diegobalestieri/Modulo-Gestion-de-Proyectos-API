package excepciones;

import org.springframework.http.HttpStatus;

public class TipoDeProyectoInvalidoException extends CustomException {
    public TipoDeProyectoInvalidoException(String mensaje) {
        super(mensaje);
        this.responseStatus = HttpStatus.BAD_REQUEST;
    }
}
