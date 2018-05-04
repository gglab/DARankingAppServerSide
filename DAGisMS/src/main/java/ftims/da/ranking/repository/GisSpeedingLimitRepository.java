package ftims.da.ranking.repository;

import ftims.da.ranking.domain.GisSpeedingLimit;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.StoredProcedureParameter;


/**
 * Spring Data JPA repository for the GisSpeedingLimit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GisSpeedingLimitRepository extends JpaRepository<GisSpeedingLimit, Long> {


    @Procedure(name="getmaxspeed", outputParameterName = "result")
    String getmaxspeed(double latitude, double longitude);


}
