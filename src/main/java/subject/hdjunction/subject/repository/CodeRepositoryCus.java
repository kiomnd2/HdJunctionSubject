package subject.hdjunction.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subject.hdjunction.subject.domain.Code;

import java.util.Optional;

public interface CodeRepositoryCus{
    Optional<Code> findById(String group, String code);
}
