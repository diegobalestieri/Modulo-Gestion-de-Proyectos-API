package persistencia;

import modelo.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IteracionesRepository extends JpaRepository<Tarea, Long> {

    //List<EntidadFase> findByProyecto(EntidadProyecto one);
}
