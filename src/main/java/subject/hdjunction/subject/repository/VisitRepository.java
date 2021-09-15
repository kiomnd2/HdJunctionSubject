package subject.hdjunction.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subject.hdjunction.subject.domain.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
