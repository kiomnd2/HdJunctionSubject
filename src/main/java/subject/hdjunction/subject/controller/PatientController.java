package subject.hdjunction.subject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subject.hdjunction.subject.codes.Codes;
import subject.hdjunction.subject.controller.response.Response;
import subject.hdjunction.subject.dto.PatientDto;
import subject.hdjunction.subject.repository.SearchCondition;
import subject.hdjunction.subject.service.PatientService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * 환자를 등록합니다.
     * @param patientDto 등록할 환자 정보
     * @return  환자 정보
     */
    @PostMapping("/api/v1/patient")
    public ResponseEntity<Response<PatientDto>> register(@RequestBody PatientDto patientDto) {
        PatientDto patient = patientService.register(patientDto);
        return ResponseEntity.ok().body(Response.success(patient));
    }

    /**
     * 환자 리스트를 조회합니다
     * @param searchCondition 리스트 조회 조건
     * @return 환자 정보 리스트
     */
    @GetMapping("/api/v1/patients")
    public ResponseEntity<Response<List<PatientDto>>> searchInfos(SearchCondition searchCondition) {
        List<PatientDto> patients = patientService.getPatients(searchCondition);
        return ResponseEntity.ok().body(Response.success(patients));
    }

    /**
     * 환자 리스트를 페이징정보를 포함하여 조회합니다
     * @param searchCondition 리스트 조회 조건
     * @param pageable 페이징 정보
     * @return 환자 정보 및 페이징 정보 리스트
     */
    @GetMapping("/api/v2/patients")
    public ResponseEntity<Response<Page<PatientDto>>> searchInfos(SearchCondition searchCondition, Pageable pageable) {
        Page<PatientDto> patients = patientService.getPatients(searchCondition, pageable);
        return ResponseEntity.ok().body(Response.success(patients));
    }

    /**
     * 환자 정보를 조회합니다.
     * @param patientId 환자 아이디
     * @return 환자 정보
     */
    @GetMapping("/api/v1/patient/{id}")
    public ResponseEntity<Response<PatientDto>> searchInfo(@PathVariable(name = "id") Long patientId) {
        return ResponseEntity.ok().body(Response.success(patientService.getPatient(patientId)));
    }

    /**
     * 환자 정보를 수정합니다
     * @param patientId 환자 아이디
     * @param patientDto 환자 수정 정보
     * @return 수정된 환자 정보
     */
    @PutMapping("/api/v1/patient/{id}")
    public ResponseEntity<Response<PatientDto>> updateInfo(@PathVariable(name = "id") Long patientId, @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok().body(Response.success(patientService.updatePatient(patientId, patientDto)));
    }

    /**
     * 환자 정보를 삭제합니다
     * @param patientId 삭제할 환자 아이디
     * @return 삭제 상태
     */
    @DeleteMapping("/api/v1/patient/{id}")
    public ResponseEntity<Response<String>> deleteInfo(@PathVariable(name = "id") Long patientId) {
        patientService.removePatient(patientId);
        return ResponseEntity.ok().body(Response.success(Codes.D2000.desc));
    }
}
