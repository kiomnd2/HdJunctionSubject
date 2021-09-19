package subject.hdjunction.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subject.hdjunction.subject.domain.CodeGroup;

public interface CodeGroupRepository extends JpaRepository<CodeGroup, String> {
}
