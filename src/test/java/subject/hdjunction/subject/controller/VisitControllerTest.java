package subject.hdjunction.subject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.Visit;
import subject.hdjunction.subject.dto.VisitDto;
import subject.hdjunction.subject.repository.HospitalRepository;
import subject.hdjunction.subject.repository.PatientRepository;
import subject.hdjunction.subject.repository.VisitRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class VisitControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    PatientRepository patientRepository;

    Hospital hospital = null;

    Patient patient = null;


    @BeforeEach
    void beforeEach() throws Throwable {
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
    void requestSearchVisit() throws Exception {

        LocalDateTime receptionDateTime = LocalDateTime.of(2021, 9,16, 12, 59,59);
        String visitStateCode = "1";
        Visit visit = Visit.builder()
                .patient(this.patient)
                .hospital(this.hospital)
                .visitStateCode(visitStateCode)
                .receptionDateTime(receptionDateTime).build();
        visitRepository.save(visit);

        mockMvc.perform(get("/api/v1/visit/" + visit.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body.id").value(visit.getId()))
                .andExpect(jsonPath("body.visitStateCode").value(visitStateCode))
                .andExpect(jsonPath("body.receptionDateTime").value(receptionDateTime.toString()));
    }

    @Test
    void requestRegisterVisit() throws Exception {

        LocalDateTime receptionDateTime = LocalDateTime.of(2021, 9,16, 12, 59,59);
        String visitStateCode = "1";
        VisitDto visit = VisitDto.builder()
                .patientId(this.patient.getId())
                .hospitalId(this.hospital.getId())
                .visitStateCode(visitStateCode)
                .receptionDateTime(receptionDateTime).build();

        mockMvc.perform(post("/api/v1/visit")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(visit)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body.visitStateCode").value(visitStateCode))
                .andExpect(jsonPath("body.receptionDateTime").value(receptionDateTime.toString()));
    }

    @Test
    void requestUpdateVisit() throws Exception {
        LocalDateTime receptionDateTime = LocalDateTime.of(2021, 9,16, 12, 59,59);
        String visitStateCode = "1";
        Visit visit = Visit.builder()
                .patient(this.patient)
                .hospital(this.hospital)
                .visitStateCode(visitStateCode)
                .receptionDateTime(receptionDateTime).build();
        Visit savedVisit = visitRepository.save(visit);


        LocalDateTime receptionDateTime2 = LocalDateTime.of(2021, 10,16, 12, 59,59);
        String visitStateCode2 = "2";
        VisitDto visit2 = VisitDto.builder()
                .patientId(this.patient.getId())
                .hospitalId(this.hospital.getId())
                .visitStateCode(visitStateCode2)
                .receptionDateTime(receptionDateTime2).build();

        mockMvc.perform(put("/api/v1/visit/" + savedVisit.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(visit2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body.visitStateCode").value(visitStateCode2))
                .andExpect(jsonPath("body.receptionDateTime").value(receptionDateTime2.toString()));
    }
}
