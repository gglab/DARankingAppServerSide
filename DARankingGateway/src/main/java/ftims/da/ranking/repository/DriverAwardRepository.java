package ftims.da.ranking.repository;

import ftims.da.ranking.domain.DriverAward;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DriverAward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverAwardRepository extends JpaRepository<DriverAward, Long> {

}
