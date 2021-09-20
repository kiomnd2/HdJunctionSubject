package subject.hdjunction.subject.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.Visit;
import subject.hdjunction.subject.dto.VisitDto;
import subject.hdjunction.subject.exception.NotFoundHospitalException;
import subject.hdjunction.subject.exception.NotFoundPatientException;
import subject.hdjunction.subject.exception.NotFoundVisitException;
import subject.hdjunction.subject.repository.HospitalRepository;
import subject.hdjunction.subject.repository.PatientRepository;
import subject.hdjunction.subject.repository.VisitRepository;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class VisitService {

    final private VisitRepository visitRepository;

    final private PatientRepository patientRepository;

    final private HospitalRepository hospitalRepository;

    /**
     * 방문 접수 정보를 등록합니다
     * @param visitDto 방문정보DTO
     * @return 방문정보
     */
    public VisitDto registerVisit(VisitDto visitDto) {

        Patient patient = patientRepository.findById(visitDto.getPatientId())
                .orElseThrow(NotFoundPatientException::new);
        Hospital hospital = hospitalRepository.findById(visitDto.getHospitalId())
                .orElseThrow(NotFoundHospitalException::new);

        Visit visits = Visit.builder()
                .receptionDateTime(visitDto.getReceptionDateTime())
                .hospital(hospital)
                .patient(patient)
                .visitStateCode(visitDto.getVisitStateCode()).build();

        Visit savedVisits = visitRepository.save(visits);

        return VisitDto.builder()
                .id(savedVisits.getId())
                .visitStateCode(savedVisits.getVisitStateCode())
                .hospitalId(savedVisits.getHospital().getId())
                .patientId(savedVisits.getPatient().getId())
                .receptionDateTime(savedVisits.getReceptionDateTime())
                .build();
    }

    /**
     * 방문 접수 정보를 가져옵니다.
     * @param visitId 방문 아이디
     * @return 방문 접수 정보
     */
    public VisitDto getVisit(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(NotFoundVisitException::new);
        return VisitDto.builder()
                .id(visit.getId())
                .visitStateCode(visit.getVisitStateCode())
                .receptionDateTime(visit.getReceptionDateTime())
                .patientId(visit.getPatient().getId())
                .hospitalId(visit.getHospital().getId())
                .build();
    }

    /**
     * 방문 접수 정보를 수정합니다.
     * @param visitId 방문 아이디
     * @param visitDto 수정할 정보
     * @return 방문 접수 정보
     */
    public VisitDto updateVisit(Long visitId, VisitDto visitDto) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(NotFoundVisitException::new);

        Visit updatedVisit = visit.updatePatient(visitDto);
        visitRepository.save(updatedVisit);
        return VisitDto.builder()
                .id(updatedVisit.getId())
                .visitStateCode(updatedVisit.getVisitStateCode())
                .receptionDateTime(updatedVisit.getReceptionDateTime())
                .patientId(updatedVisit.getPatient().getId())
                .hospitalId(updatedVisit.getHospital().getId())
                .build();
    }

    /**
     * 방문 접소 정보를 제거합니다.
     * @param visitId 방문 아이디
     */
    public void removeVisit(Long visitId) {
        visitRepository.deleteById(visitId);
    }
}
