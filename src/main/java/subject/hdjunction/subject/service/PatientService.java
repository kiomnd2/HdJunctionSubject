package subject.hdjunction.subject.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.Visit;
import subject.hdjunction.subject.dto.PatientDto;
import subject.hdjunction.subject.dto.VisitDto;
import subject.hdjunction.subject.exception.NotFoundHospitalException;
import subject.hdjunction.subject.exception.NotFoundPatientException;
import subject.hdjunction.subject.repository.HospitalRepository;
import subject.hdjunction.subject.repository.PatientRepository;
import subject.hdjunction.subject.repository.SearchCondition;
import subject.hdjunction.subject.repository.VisitRepository;
import subject.hdjunction.subject.util.PatientNoGenerator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PatientService {

    final private PatientRepository patientRepository;

    final private HospitalRepository hospitalRepository;

    final private VisitRepository visitRepository;


    @Transactional(readOnly = false)
    public PatientDto register(PatientDto patientDto) {

        Hospital hospital = hospitalRepository.findById(patientDto.getHospitalId())
                .orElseThrow(NotFoundHospitalException::new);

        String patientNo = checkPatientNo(hospital);

        Patient patient = Patient.builder()
                .patientName(patientDto.getPatientName())
                .birthDate(patientDto.getBirthDate())
                .genderCode(patientDto.getGenderCode())
                .hospital(hospital)
                .patientNo(patientNo)
                .phoneNumber(patientDto.getPhoneNumber())
                .build();

        Patient savedPatient = patientRepository.save(patient);

        return PatientDto.builder()
                .id(savedPatient.getId())
                .patientName(savedPatient.getPatientName())
                .birthDate(savedPatient.getBirthDate())
                .genderCode(savedPatient.getGenderCode())
                .hospitalId(savedPatient.getHospital().getId())
                .patientNo(savedPatient.getPatientNo())
                .phoneNumber(savedPatient.getPhoneNumber())
                .build();
    }

    @Transactional(readOnly = false)
    public void removePatient(Long id) {
        patientRepository.deleteById(id);
    }


    public PatientDto getPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(NotFoundPatientException::new);

        List<Visit> patientVisits = visitRepository.findAllByPatient(patient);
        List<VisitDto> visitInfos = patientVisits.stream()
                .map(visit -> VisitDto.builder()
                        .id(visit.getId())
                        .hospitalId(patient.getHospital().getId())
                        .patientId(patient.getId())
                        .receptionDateTime(visit.getReceptionDateTime())
                        .visitStateCode(visit.getVisitStateCode())
                        .build()).collect(Collectors.toList());

        PatientDto patientDto = PatientDto.builder()
                .id(patient.getId())
                .patientName(patient.getPatientName())
                .phoneNumber(patient.getPhoneNumber())
                .patientNo(patient.getPatientNo())
                .hospitalId(patient.getHospital().getId())
                .genderCode(patient.getGenderCode())
                .birthDate(patient.getBirthDate())
                .build();
        patientDto.addVisitDtos(visitInfos);

        return patientDto;
    }


    public List<PatientDto> getPatients() {
        return getPatients(new SearchCondition());
    }

    public List<PatientDto> getPatients(SearchCondition searchCondition) {
        return patientRepository.findBySearchCondition(searchCondition);
    }


    public Page<PatientDto> getPatients(SearchCondition searchCondition, Pageable pageable) {
        return patientRepository.findBySearchConditionAndPageable(searchCondition, pageable);
    }

    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        Patient patient = patientRepository.findById(id).orElseThrow(NotFoundPatientException::new);
        Patient updatedPatient = patientRepository.save(patient.updatePatient(patientDto));
        return PatientDto.builder()
                .id(updatedPatient.getId())
                .patientName(updatedPatient.getPatientName())
                .hospitalId(updatedPatient.getHospital().getId())
                .birthDate(updatedPatient.getBirthDate())
                .genderCode(updatedPatient.getGenderCode())
                .patientNo(updatedPatient.getPatientNo())
                .phoneNumber(updatedPatient.getPhoneNumber())
                .build();
    }

    public String checkPatientNo(Hospital hospital) {
        String patientNo = PatientNoGenerator.generate();
        if (!patientRepository.existsByHospitalAndPatientNo(hospital, patientNo)) {
            return patientNo;
        }
        return checkPatientNo(hospital);
    }

}
