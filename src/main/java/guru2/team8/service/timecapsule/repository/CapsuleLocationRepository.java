package guru2.team8.service.timecapsule.repository;

import guru2.team8.service.timecapsule.domain.CapsuleLocation;
import guru2.team8.service.timecapsule.domain.Timecapsule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CapsuleLocationRepository extends JpaRepository<CapsuleLocation, Long> {
    Optional<CapsuleLocation> findByTimeCapsuleId(Long timeCapsuleId);
    void deleteByTimeCapsuleId(Long id);
}
