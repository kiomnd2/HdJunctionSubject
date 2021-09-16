package subject.hdjunction.subject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.Visit;
import subject.hdjunction.subject.dto.VisitDto;
import subject.hdjunction.subject.repository.HospitalRepository;
import subject.hdjunction.subject.repository.PatientRepository;
import subject.hdjunction.subject.repository.VisitRepository;

import java.time.LocalDateTime;


@Transactional
@SpringBootTest
class VisitServiceTest {

    @Autowired
    VisitService visitService;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    PatientRepository patientRepository;

    Hospital hospital = null;

    Patient patient = null;

    @BeforeEach
    void beforeEach() {
        visitRepository.deleteAll();
        hospitalRepository.deleteAll();
        patientRepository.deleteAll();
        saveHospital();
        savePatient();
    }

    void saveHospital() {

        final String hospitalName = "김병원";
        final String chiefName = "김길동";
        String nursingHomeNo = "1234567";
        Hospital hospital = Hospital.builder()
                .hospitalName(hospitalName)
                .chiefName(chiefName)
                .nursingHomeNo(nursingHomeNo)
                .build();

        this.hospital = hospitalRepository.save(hospital);
    }

    void savePatient() {
        final String patientName = "김개똥";
        final String patientNo = "123123";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";


        Patient patient = Patient.builder()
                .patientName(patientName)
                .patientNo(patientNo)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .phoneNumber(phoneNo)
                .hospital(this.hospital)
                .build();

        this.patient = patientRepository.save(patient);
    }

    @Test
    void registerVisitTest() throws Throwable{
        LocalDateTime receptionDateTime = LocalDateTime.of(2021, 9,16, 12, 59,59);
        final String visitStateCode = "2";
        VisitDto visitDto = VisitDto.builder()
                .patientId(this.patient.getId())
                .hospitalId(this.hospital.getId())
                .receptionDateTime(receptionDateTime)
                .visitStateCode(visitStateCode).build();

        VisitDto visitDto1 = visitService.registerVisit(visitDto);

        Visit visit = visitRepository.findAll().get(0);

        Assertions.assertThat(visit.getHospital().getId()).isEqualTo(this.hospital.getId());
        Assertions.assertThat(visit.getPatient().getId()).isEqualTo(this.patient.getId());
        Assertions.assertThat(visit.getVisitStateCode()).isEqualTo(visitStateCode);
        Assertions.assertThat(visit.getReceptionDateTime()).isEqualTo(receptionDateTime);

    }

    @Test
    void visitInfoTest() throws Throwable {
        LocalDateTime receptionDateTime = LocalDateTime.of(2021, 9,16, 12, 59,59);
        final String visitStateCode = "2";
        VisitDto visitDto = VisitDto.builder()
                .patientId(this.patient.getId())
                .hospitalId(this.hospital.getId())
                .receptionDateTime(receptionDateTime)
                .visitStateCode(visitStateCode).build();

        VisitDto visitDto1 = visitService.registerVisit(visitDto);

        VisitDto visit = visitService.getVisit(visitDto1.getId());

        Assertions.assertThat(visit.getVisitStateCode()).isEqualTo(visitDto.getVisitStateCode());
        Assertions.assertThat(visit.getReceptionDateTime()).isEqualTo(visitDto.getReceptionDateTime());
    }

    @Test
    void updateVisitTest() throws Throwable {
        LocalDateTime receptionDateTime = LocalDateTime.of(2021, 9,16, 12, 59,59);
        final String visitStateCode = "2";
        VisitDto visitDto = VisitDto.builder()
                .patientId(this.patient.getId())
                .hospitalId(this.hospital.getId())
                .receptionDateTime(receptionDateTime)
                .visitStateCode(visitStateCode).build();

        visitService.registerVisit(visitDto);

        Visit visit = visitRepository.findAll().get(0);

        final LocalDateTime receptionDateTime2 = receptionDateTime.plusHours(1);
        final String visitStateCode2 = "1";
        VisitDto toVisitDto = VisitDto.builder()
                .visitStateCode(visitStateCode2)
                .receptionDateTime(receptionDateTime2).build();

        VisitDto visitDto2 = visitService.updateVisit(visit.getId(), toVisitDto);

        Visit visit1 = visitRepository.findById(visitDto2.getId()).get();

        Assertions.assertThat(visitDto2.getVisitStateCode()).isEqualTo(visitStateCode2);
        Assertions.assertThat(visitDto2.getReceptionDateTime()).isEqualTo(receptionDateTime2);
    }
}
