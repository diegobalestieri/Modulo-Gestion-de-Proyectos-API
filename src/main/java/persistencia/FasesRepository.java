package persistencia;

import modelo.Fase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FasesRepository extends JpaRepository<Fase, Long> {

    //List<EntidadFase> findByProyecto(EntidadProyecto one);
}
