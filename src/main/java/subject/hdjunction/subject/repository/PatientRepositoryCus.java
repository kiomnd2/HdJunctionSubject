package subject.hdjunction.subject.repository;

import subject.hdjunction.subject.dto.PatientDto;

import java.util.List;

public interface PatientRepositoryCus {
    List<PatientDto> findBySearchCondition(SearchCondition searchCondition);
}
