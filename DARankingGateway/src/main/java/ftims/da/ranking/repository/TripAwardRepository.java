package ftims.da.ranking.repository;

import ftims.da.ranking.domain.TripAward;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TripAward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripAwardRepository extends JpaRepository<TripAward, Long> {

}
