package persistencia;

import modelo.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProyectosRepository extends JpaRepository<Proyecto, Long> {

}
