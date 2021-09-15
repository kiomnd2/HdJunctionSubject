package subject.hdjunction.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subject.hdjunction.subject.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
