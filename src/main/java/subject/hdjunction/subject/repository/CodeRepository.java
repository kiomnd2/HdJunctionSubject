package subject.hdjunction.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subject.hdjunction.subject.domain.Code;
import subject.hdjunction.subject.domain.CodeGroup;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, String>, CodeRepositoryCus{
}
