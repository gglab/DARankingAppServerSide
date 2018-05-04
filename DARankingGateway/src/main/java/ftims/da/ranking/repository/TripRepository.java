package ftims.da.ranking.repository;

import ftims.da.ranking.domain.Trip;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("select distinct trip from Trip trip left join fetch trip.awards")
    List<Trip> findAllWithEagerRelationships();

    @Query("select trip from Trip trip left join fetch trip.awards where trip.id =:id")
    Trip findOneWithEagerRelationships(@Param("id") Long id);

}
