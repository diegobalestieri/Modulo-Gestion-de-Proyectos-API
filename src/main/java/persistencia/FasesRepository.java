package persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FasesRepository extends JpaRepository<EntidadFase, Long> {

}
