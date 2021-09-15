package subject.hdjunction.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subject.hdjunction.subject.domain.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital,Long> {
}
