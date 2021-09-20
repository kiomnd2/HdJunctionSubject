package subject.hdjunction.subject.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.consts.CodeGroupConstant;
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

    final private CodeManager codeManager;


    /**
     * 환자정보를 등록합니다
     * @param patientDto 환자정보
     * @return 환자정보DTO
     */
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
                .genderName(codeManager.getCodeName(CodeGroupConstant.GENDER
                        , savedPatient.getGenderCode()))
                .hospitalId(savedPatient.getHospital().getId())
                .patientNo(savedPatient.getPatientNo())
                .phoneNumber(savedPatient.getPhoneNumber())
                .build();
    }

    /**
     * 환자정보를 제거합니다
     * @param id 환자아이디
     */
    @Transactional(readOnly = false)
    public void removePatient(Long id) {
        patientRepository.deleteById(id);
    }


    /**
     * 환자정보를 가져옵니다
     * @param patientId 환자 아이디
     * @return 환자정보DTO
     */
    public PatientDto getPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(NotFoundPatientException::new);

        List<Visit> patientVisits = visitRepository.findAllByPatientOrderByReceptionDateTimeDesc(patient);
        List<VisitDto> visitInfos = patientVisits.stream()
                .map(visit -> VisitDto.builder()
                        .id(visit.getId())
                        .hospitalId(patient.getHospital().getId())
                        .patientId(patient.getId())
                        .receptionDateTime(visit.getReceptionDateTime())
                        .visitStateCode(visit.getVisitStateCode())
                        .visitStateName(codeManager.getCodeName(CodeGroupConstant.VISIT,visit.getVisitStateCode()))
                        .build()).collect(Collectors.toList());

        PatientDto patientDto = PatientDto.builder()
                .id(patient.getId())
                .patientName(patient.getPatientName())
                .phoneNumber(patient.getPhoneNumber())
                .patientNo(patient.getPatientNo())
                .hospitalId(patient.getHospital().getId())
                .genderCode(patient.getGenderCode())
                .genderName(codeManager.getCodeName(CodeGroupConstant.GENDER
                        , patient.getGenderCode()))
                .birthDate(patient.getBirthDate())
                .build();
        patientDto.addVisitDtos(visitInfos);
        if (visitInfos.size() > 0) patientDto.setLastReceptionDateTime(visitInfos.get(0).getReceptionDateTime());
        return patientDto;
    }


    /**
     * 환자 정보 리스트를 가져옵니다.
     * @return
     */
    public List<PatientDto> getPatients() {
        return getPatients(new SearchCondition());
    }

    /**
     * 환자정보리스트를 조건에 따라 출력합니다
     * @param searchCondition 조회조건
     * @return 환자정보리스트
     */
    public List<PatientDto> getPatients(SearchCondition searchCondition) {
        return patientRepository.findBySearchCondition(searchCondition);
    }

    /**
     * 환자정보를 조건에따라 페이징 처리하여 가져옵니다
     * @param searchCondition 조회 조건
     * @param pageable 페이징
     * @return 페이징된 환자정보리스트
     */
    public Page<PatientDto> getPatients(SearchCondition searchCondition, Pageable pageable) {
        return patientRepository.findBySearchConditionAndPageable(searchCondition, pageable);
    }

    /**
     * 환자정보를 수정합니다.
     * @param id 환자 아이디
     * @param patientDto 수정할 환자정보
     * @return 환자정보DTO
     */
    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        Patient patient = patientRepository.findById(id).orElseThrow(NotFoundPatientException::new);
        Patient updatedPatient = patientRepository.save(patient.updatePatient(patientDto));
        return PatientDto.builder()
                .id(updatedPatient.getId())
                .patientName(updatedPatient.getPatientName())
                .hospitalId(updatedPatient.getHospital().getId())
                .birthDate(updatedPatient.getBirthDate())
                .genderCode(updatedPatient.getGenderCode())
                .genderName(codeManager.getCodeName(CodeGroupConstant.GENDER
                        , updatedPatient.getGenderCode()))
                .patientNo(updatedPatient.getPatientNo())
                .phoneNumber(updatedPatient.getPhoneNumber())
                .build();
    }

    /**
     * 환자번호를 랜덤으로 생성하고 중복을 학인하여 출력합니다.
     * @param hospital 병원정보
     * @return 랜덤키값
     */
    public String checkPatientNo(Hospital hospital) {
        String patientNo = PatientNoGenerator.generate();
        if (!patientRepository.existsByHospitalAndPatientNo(hospital, patientNo)) {
            return patientNo;
        }
        return checkPatientNo(hospital);
    }

}
