package ftims.da.ranking.repository;

import ftims.da.ranking.domain.Driver;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Driver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query("select distinct driver from Driver driver left join fetch driver.awards")
    List<Driver> findAllWithEagerRelationships();

    @Query("select driver from Driver driver left join fetch driver.awards where driver.id =:id")
    Driver findOneWithEagerRelationships(@Param("id") Long id);

}
