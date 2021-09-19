package subject.hdjunction.subject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.codes.Codes;
import subject.hdjunction.subject.controller.response.Response;
import subject.hdjunction.subject.domain.Hospital;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.Visit;
import subject.hdjunction.subject.dto.PatientDto;
import subject.hdjunction.subject.exception.NotFoundHospitalException;
import subject.hdjunction.subject.repository.HospitalRepository;
import subject.hdjunction.subject.repository.PatientRepository;
import subject.hdjunction.subject.repository.SearchCondition;
import subject.hdjunction.subject.repository.VisitRepository;
import subject.hdjunction.subject.service.PatientService;


import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureRestDocs
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

    @Autowired
    PatientService patientService;

    @Autowired
    VisitRepository visitRepository;

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
        final String patientNo = "123456";
        final String phoneNo = "01111123123";

        Patient patient = Patient.builder()
                .patientName(patientName)
                .birthDate(birthDate)
                .genderCode(genderCode)
                .phoneNumber(phoneNo)
                .patientNo(patientNo)
                .hospital(this.hospital)
                .build();

        Patient patientEntity = patientRepository.save(patient);


        LocalDateTime originReceptionDateTime = LocalDateTime.of(2021, 9,1, 12, 59,59);

        for (int i=0 ; i< 10 ; i++) {
            LocalDateTime receptionDateTime = originReceptionDateTime.minusDays(i);
            final String visitStateCode = Integer.toString(i);
            Visit visit = Visit.builder()
                    .receptionDateTime(receptionDateTime)
                    .visitStateCode(visitStateCode)
                    .hospital(this.hospital)
                    .patient(patient)
                    .build();

            visitRepository.save(visit);
        }


        mockMvc.perform(get("/api/v1/patient/{id}", Long.toString(patientEntity.getId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andDo(document("patient",
                        pathParameters(parameterWithName("id").description("아이디"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("body.id").value(patientEntity.getId()))
                .andExpect(jsonPath("body.hospitalId").value(patientEntity.getHospital().getId()))
                .andExpect(jsonPath("body.patientName").value(patientEntity.getPatientName()))
                .andExpect(jsonPath("body.patientNo").value(patientEntity.getPatientNo()))
                .andExpect(jsonPath("body.lastReceptionDateTime").value(originReceptionDateTime.toString()))
        ;
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
        final String genderCode2 = "M";
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


        mockMvc.perform(get("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andDo(document("patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("body").isArray())
                .andExpect(jsonPath("body.size()").value(2))
                .andExpect(jsonPath("body[*].patientName", contains(patientName,
                        patientName2)))
                .andExpect(jsonPath("body[*].genderCode", contains(genderCode,
                        genderCode2)))
                .andExpect(jsonPath("body[*].birthDate", contains(birthDate,
                        birthDate2)));
    }
    @Test
    void requestRegisterPatient() throws Exception {

        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .phoneNumber(phoneNo)
                .genderCode(genderCode)
                .birthDate(birthDate)
                .hospitalId(this.hospital.getId()).build();

        mockMvc.perform(post("/api/v1/patient")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(patientDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("register-patient",
                        requestFields(
                                fieldWithPath("hospitalId").description("병원아이디"),
                                fieldWithPath("patientName").description("환자명"),
                                fieldWithPath("genderCode").description("성별코드"),
                                fieldWithPath("birthDate").description("생년월일"),
                                fieldWithPath("phoneNumber").description("휴대전화번호")
                        )
                ));

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
    void requestSearchPatientsByConditionsAndPaging() throws Exception {
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

        SearchCondition searchCondition = new SearchCondition();
        MvcResult mvcResult = mockMvc.perform(get("/api/v2/patients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("pageNo", "1")
                .param("pageSize", "5")
                .param("patientName", "김개똥0")
                .param("patientNo", "123120")
                .param("birthDate", "19910220"))
                .andDo(print())
                .andDo(document("list-patient",
                        requestParameters(
                                parameterWithName("pageNo").description("페이지번호"),
                                parameterWithName("pageSize").description("페이지수"),
                                parameterWithName("patientName").description("환자명"),
                                parameterWithName("patientNo").description("환자등록번호"),
                                parameterWithName("birthDate").description("생년월일")
                                )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("body.content").value(hasSize(1)))
                .andExpect(jsonPath("body.content[*].patientName")
                        .value(Matchers.contains("김개똥0")))
                .andExpect(jsonPath("body.pageable.offset").value(0))
                .andExpect(jsonPath("body.last").value(true))
                .andReturn();
    }

    @Test
    void requestUpdatePatient() throws Exception {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .phoneNumber(phoneNo)
                .genderCode(genderCode)
                .birthDate(birthDate)
                .hospitalId(this.hospital.getId()).build();

        PatientDto registerPatient = patientService.register(patientDto);

        final String toPatientName = "김개똥2";
        final String toBirthDate = "19910222";
        final String toPhoneNo = "0111234123";

        PatientDto toPatientDto = PatientDto.builder()
                .patientName(toPatientName)
                .phoneNumber(toPhoneNo)
                .birthDate(toBirthDate)
                .hospitalId(this.hospital.getId()).build();


        mockMvc.perform(put("/api/v1/patient/{id}", registerPatient.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(toPatientDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-patient",
                        pathParameters(
                                parameterWithName("id").description("환자아이디")
                        ),
                        requestFields(
                                fieldWithPath("hospitalId").description("병원아이디"),
                                fieldWithPath("patientName").description("환자명"),
                                fieldWithPath("birthDate").description("생년월일"),
                                fieldWithPath("phoneNumber").description("휴대전화번호")
                        )
                ));
    }

    @Test
    void requestDeletePatient() throws Exception {
        final String patientName = "김개똥";
        final String birthDate = "19910221";
        final String genderCode = "M";
        final String phoneNo = "01111123123";

        PatientDto patientDto = PatientDto.builder()
                .patientName(patientName)
                .phoneNumber(phoneNo)
                .genderCode(genderCode)
                .birthDate(birthDate)
                .hospitalId(this.hospital.getId()).build();

        PatientDto registerPatient = patientService.register(patientDto);

        mockMvc.perform(delete("/api/v1/patient/{id}", registerPatient.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-patient",
                        pathParameters(
                                parameterWithName("id").description("환자아이디")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("body").value("삭제"));

    }
}
