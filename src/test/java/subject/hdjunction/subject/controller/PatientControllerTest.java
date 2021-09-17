package subject.hdjunction.subject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import subject.hdjunction.subject.codes.Codes;
import subject.hdjunction.subject.controller.response.Response;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.dto.PatientDto;
import subject.hdjunction.subject.exception.NotFoundHospitalException;
import subject.hdjunction.subject.repository.HospitalRepository;
import subject.hdjunction.subject.repository.PatientRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    PatientRepository patientRepository;

    Hospital hospital;

    @BeforeEach
    void beforeEach() throws Throwable{
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
    void requestSearchPatient() throws Exception {

        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        Patient patient = Patient.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .phoneNumber(phoneNo)
                .hospital(this.hospital)
                .build();

        Patient patientEntity = patientRepository.save(patient);

        mockMvc.perform(get("/api/v1/patient/" + patientEntity.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body.id").value(patientEntity.getId()))
                .andExpect(jsonPath("body.hospitalId").value(patientEntity.getHospital().getId()))
                .andExpect(jsonPath("body.patientName").value(patientEntity.getPatientName()))
                .andExpect(jsonPath("body.patientNo").value(patientEntity.getPatientNo()));
    }

    @Test
    void requestSearchPatients() throws Exception {

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

        Patient patientEntity = patientRepository.save(patient);


        final String patientName2 = "김개똥2";
        final String patientNo2 = "123";
        final String birthDate2 = "19910";
        final String genderCode2 = "W";
        final String phoneNo2 = "0111111233";

        Patient patient2 = Patient.builder()
                .patientName(patientName2)
                .patientNo(patientNo2)
                .birthDate(birthDate2)
                .genderCode(genderCode2)
                .phoneNumber(phoneNo2)
                .hospital(this.hospital)
                .build();

        Patient patientEntity2 = patientRepository.save(patient2);


        mockMvc.perform(get("/api/vi/patients")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body[0].id").value(patientEntity.getId()))
                .andExpect(jsonPath("body[1].id").value(patientEntity2.getId()));
    }
    @Test
    void requestSearchPatientNotFoundFail() throws Exception {
        mockMvc.perform(get("/api/v1/patient/" + 122)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void requestRegisterPatient() throws Exception {

        final String patientName = "김개똥";
        final String patientNo = "123123";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .phoneNumber(phoneNo)
                .patientNo(patientNo)
                .genderCode(genderCode)
                .birthDate(birthDate)
                .hospitalId(this.hospital.getId()).build();

        mockMvc.perform(post("/api/v1/patient")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(patientDto)))
                .andExpect(status().isOk())
                .andDo(print());

        Patient patient = patientRepository.findAll().get(0);

        Assertions.assertThat(patient.getPatientName()).isEqualTo(patientName);
        Assertions.assertThat(patient.getPhoneNumber()).isEqualTo(phoneNo);
        Assertions.assertThat(patient.getHospital().getId()).isEqualTo(this.hospital.getId());
    }

    @Test
    void requestRegisterPatientNotFoundHospital() throws Exception {

        final String patientName = "김개똥";
        final String patientNo = "123123";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .phoneNumber(phoneNo)
                .patientNo(patientNo)
                .genderCode(genderCode)
                .birthDate(birthDate)
                .hospitalId(123L).build();

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/patient")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(patientDto)))
                .andDo(print())
                .andExpect(status().isNotFound()).andReturn();

        Response<Object> response = mapper.readValue(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertThat(response.getBody()).isEqualTo(new NotFoundHospitalException().getMessage());
        Assertions.assertThat(response.getCode()).isEqualTo(Codes.E4000.code);
    }

    @Test
    void requestSearchPatientsByConditions() throws Exception {

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

        Patient patientEntity = patientRepository.save(patient);


        final String patientName2 = "김개똥2";
        final String patientNo2 = "123";
        final String birthDate2 = "19910";
        final String genderCode2 = "W";
        final String phoneNo2 = "0111111233";

        Patient patient2 = Patient.builder()
                .patientName(patientName2)
                .patientNo(patientNo2)
                .birthDate(birthDate2)
                .genderCode(genderCode2)
                .phoneNumber(phoneNo2)
                .hospital(this.hospital)
                .build();

        patientRepository.save(patient2);


        MvcResult mvcResult = mockMvc.perform(get("/api/vi/patients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("birthDate", birthDate2)
                .param("patientName", patientName2)
                .param("patientNo", patientNo2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body[0].id").value(patient2.getId()))
                .andExpect(jsonPath("body[0].hospitalId").value(patient2.getHospital().getId()))
                .andExpect(jsonPath("body[0].patientNo").value(patient2.getPatientNo()))
                .andExpect(jsonPath("body[0].genderCode").value(patient2.getGenderCode()))
                .andExpect(jsonPath("body[0].birthDate").value(patient2.getBirthDate()))
                .andReturn();

        Response<List<PatientDto>> result = mapper.readValue(mvcResult.getResponse().getContentAsString(), Response.class);

        List<PatientDto> list = result.getBody();

        Assertions.assertThat(list.size()).isEqualTo(1);
    }


}
