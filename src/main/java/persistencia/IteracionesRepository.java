package persistencia;

import modelo.Iteracion;
import modelo.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IteracionesRepository extends JpaRepository<Iteracion, Long> {

    //List<EntidadFase> findByProyecto(EntidadProyecto one);
}
