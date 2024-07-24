package guru2.team8.service.timecapsule.repository;

import guru2.team8.service.timecapsule.domain.Timecapsule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimecapsuleRepository extends JpaRepository<Timecapsule, Long> {
}
