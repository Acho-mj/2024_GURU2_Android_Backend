package guru2.team8.service.timecapsule.repository;

import guru2.team8.service.timecapsule.domain.CapsuleLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapsuleLocationRepository extends JpaRepository<CapsuleLocation, Long> {
}
