package subject.hdjunction.subject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import subject.hdjunction.subject.dto.PatientDto;

import java.util.List;

public interface PatientRepositoryCus {
    List<PatientDto> findBySearchCondition(SearchCondition searchCondition);
    Page<PatientDto> findBySearchConditionAndPageable(SearchCondition searchCondition, Pageable pageable);
}
