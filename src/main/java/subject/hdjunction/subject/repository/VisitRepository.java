package subject.hdjunction.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.Visit;

import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAllByPatientOrderByReceptionDateTimeDesc(Patient patient);

    Optional<Visit> findTopByPatientOrderByReceptionDateTimeDesc(Patient patient);
}
