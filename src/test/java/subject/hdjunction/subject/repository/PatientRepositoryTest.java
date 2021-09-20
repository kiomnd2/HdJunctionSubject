package subject.hdjunction.subject.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.dto.PatientDto;

import java.util.List;


@Transactional
@SpringBootTest
class PatientRepositoryTest {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    HospitalRepository hospitalRepository;


    Hospital hospital = null;

    @BeforeEach
    void beforeEach() {
        patientRepository.deleteAll();
        hospitalRepository.deleteAll();
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
    void insertPatientInfoTest() throws Throwable {
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

        patientRepository.save(patient);

        Patient selectedPatient = patientRepository.findAll().get(0);


        Assertions.assertThat(selectedPatient.getPatientName()).isEqualTo(patientName);
        Assertions.assertThat(selectedPatient.getBirthDate()).isEqualTo(birthDate);
        Assertions.assertThat(selectedPatient.getHospital().getChiefName()).isEqualTo(this.hospital.getChiefName());
        Assertions.assertThat(selectedPatient.getHospital().getHospitalName()).isEqualTo(this.hospital.getHospitalName());

    }


    @Test
    void findPatientBySearchCondition() throws Throwable {
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

        final String patientName2 = "김개똥";
        final String patientNo2 = "1231232";
        final String birthDate2 = "19910221";
        final String genderCode2 = "M";
        final String phoneNo2 = "01111123123";

        Patient patient2 = Patient.builder()
                .patientName(patientName2)
                .patientNo(patientNo2)
                .birthDate(birthDate2)
                .genderCode(genderCode2)
                .phoneNumber(phoneNo2)
                .hospital(this.hospital)
                .build();

        patientRepository.save(patient2);

        SearchCondition condition = new SearchCondition();
        condition.setPatientNo(patientNo);
        condition.setBirthDate(birthDate);
        List<PatientDto> bySearchCondition = patientRepository.findBySearchCondition(condition);
        System.out.println("bySearchCondition = " + bySearchCondition);

        Assertions.assertThat(bySearchCondition.size()).isEqualTo(1);
        Assertions.assertThat(bySearchCondition.get(0).getPatientName()).isEqualTo(patientName);
        Assertions.assertThat(bySearchCondition.get(0).getBirthDate()).isEqualTo(birthDate);
        Assertions.assertThat(bySearchCondition.get(0).getGenderCode()).isEqualTo(genderCode);
    }

}
