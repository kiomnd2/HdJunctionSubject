package subject.hdjunction.subject.service;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.Visit;
import subject.hdjunction.subject.dto.PatientDto;
import subject.hdjunction.subject.repository.HospitalRepository;
import subject.hdjunction.subject.repository.PatientRepository;
import subject.hdjunction.subject.repository.SearchCondition;
import subject.hdjunction.subject.repository.VisitRepository;

import java.time.LocalDateTime;
import java.util.List;


@Transactional
@SpringBootTest
class PatientServiceTest {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientService patientService;

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    VisitRepository visitRepository;

    Hospital hospital;

    @BeforeEach
    void beforeEach() throws Throwable{
        patientRepository.deleteAll();
        saveHospital();
    }
    void saveHospital() {
        Hospital hospital = Hospital.builder()
                .hospitalName("김병원")
                .chiefName("김길동")
                .nursingHomeNo("1234567")
                .build();

        this.hospital = hospitalRepository.save(hospital);
    }


    @Test
    void patientRegisterTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        PatientDto registerDto = patientService.register(patientDto);

        Patient patient = patientRepository.findById(registerDto.getId()).get();

        Assertions.assertThat(registerDto.getPatientName()).isEqualTo(patient.getPatientName());
        Assertions.assertThat(registerDto.getBirthDate()).isEqualTo(patient.getBirthDate());
        Assertions.assertThat(registerDto.getGenderCode()).isEqualTo(patient.getGenderCode());
        Assertions.assertThat(registerDto.getHospitalId()).isEqualTo(patient.getHospital().getId());
    }

    @Test
    void patientRemoveTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        PatientDto registerDto = patientService.register(patientDto);

        patientService.removePatient(registerDto.getId());
    }

    @Test
    void patientInfoTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        PatientDto registerDto = patientService.register(patientDto);

        PatientDto patient = patientService.getPatient(registerDto.getId());

        Assertions.assertThat(patient.getPatientName()).isEqualTo(registerDto.getPatientName());
        Assertions.assertThat(patient.getHospitalId()).isEqualTo(registerDto.getHospitalId());
        Assertions.assertThat(patient.getGenderCode()).isEqualTo(registerDto.getGenderCode());
        Assertions.assertThat(patient.getLastReceptionDateTime()).isEqualTo(registerDto.getLastReceptionDateTime());
    }

    @Test
    void patientInfosTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        patientService.register(patientDto);

        final String patientName2 = "김개똥2";
        final String birthDate2 = "19910221";
        final String genderCode2 = "M";
        final String phoneNo2 = "01111123123";

        PatientDto patientDto2 = PatientDto.builder()
                .patientName(patientName2)
                .birthDate(birthDate2)
                .genderCode(genderCode2)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo2).build();

        patientService.register(patientDto2);



        List<PatientDto> patients = patientService.getPatients();

        Assertions.assertThat(patients.size()).isEqualTo(2);
        Assertions.assertThat(patients.get(0).getPatientName()).isEqualTo(patientName);
        Assertions.assertThat(patients.get(1).getPatientName()).isEqualTo(patientName2);
    }

    @Test
    void updatePatientInfoTest() throws Throwable {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .hospitalId(this.hospital.getId())
                .phoneNumber(phoneNo).build();

        PatientDto register = patientService.register(patientDto);

        final String toPatientName = "김길동";
        final String toBirthDate = "19910502";
        final String toPhoneNo = "01112343123";

        PatientDto toUpdatePatientDto = PatientDto.builder()
                .patientName(toPatientName)
                .birthDate(toBirthDate)
                .phoneNumber(toPhoneNo).build();

        patientService.updatePatient(register.getId(), toUpdatePatientDto);

        Patient patient = patientRepository.findAll().get(0);

        Assertions.assertThat(patient.getPatientName()).isEqualTo(toPatientName);
        Assertions.assertThat(patient.getBirthDate()).isEqualTo(toBirthDate);
        Assertions.assertThat(patient.getPhoneNumber()).isEqualTo(toPhoneNo);

    }


    @Test
    void findPatientsAndLastVisitsDateTest() throws Throwable {
        final String patientName = "김개똥122";
        final String patientNo = "123123";
        final String birthDate = "19910222";
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

        patientRepository.save(patient);

        LocalDateTime receptionDateTime = LocalDateTime.of(2021, 9,1, 12, 59,59);
        for (int i=0 ; i< 10 ; i++) {
            LocalDateTime changedDateTime = receptionDateTime.plusDays(i);
            final String visitStateCode = Integer.toString(i);
            Visit visit = Visit.builder()
                    .receptionDateTime(changedDateTime)
                    .visitStateCode(visitStateCode)
                    .hospital(this.hospital)
                    .patient(patient)
                    .build();

            visitRepository.save(visit);
        }


        List<PatientDto> patients = patientService.getPatients();

        Assertions.assertThat(patients.size()).isEqualTo(1);
        Assertions.assertThat(patients.get(0).getPatientName()).isEqualTo(patientName);
        Assertions.assertThat(patients.get(0).getPatientNo()).isEqualTo(patientNo);
        Assertions.assertThat(patients.get(0).getGenderCode()).isEqualTo(genderCode);
        Assertions.assertThat(patients.get(0).getLastReceptionDateTime()).isEqualTo(receptionDateTime.plusDays(9).toString());
    }

    @Test
    void findPatientsAndPagingTest() throws Throwable {
        String patientName = "김개똥";
        String patientNo = "12312";
        String birthDate = "1991022";
        String phoneNo = "0111112312";
        String genderCode = "M";
        for(int i=0 ; i< 10 ; i++ ){
            patientName = "김개똥"+i;
            patientNo = "12312"+i;
            birthDate = "1991022"+i;
            genderCode = "M";
            phoneNo = "0111112312"+i;

            Patient patient = Patient.builder()
                    .patientName(patientName)
                    .patientNo(patientNo)
                    .birthDate(birthDate)
                    .genderCode(genderCode)
                    .phoneNumber(phoneNo)
                    .hospital(this.hospital)
                    .build();
            patientRepository.save(patient);
        }

        SearchCondition condition = new SearchCondition();

        PageRequest request = PageRequest.of(1, 5);
        Pageable pageable = request.toOptional().get();

        Page<PatientDto> page = patientService.getPatients(condition, pageable);
        List<PatientDto> patients = page.getContent();

        Assertions.assertThat(patients.size()).isEqualTo(5);
        Assertions.assertThat(patients.get(0).getPatientName()).isEqualTo("김개똥5");
        Assertions.assertThat(patients.get(0).getPatientNo()).isEqualTo("123125");
    }

}
