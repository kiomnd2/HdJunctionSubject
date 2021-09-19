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

    @PostMapping("/api/v1/patient")
    public ResponseEntity<Response<PatientDto>> register(@RequestBody PatientDto patientDto) {
        PatientDto patient = patientService.register(patientDto);
        return ResponseEntity.ok().body(Response.success(patient));
    }

    @GetMapping("/api/v1/patients")
    public ResponseEntity<Response<List<PatientDto>>> searchInfos(SearchCondition searchCondition) {
        List<PatientDto> patients = patientService.getPatients(searchCondition);
        return ResponseEntity.ok().body(Response.success(patients));
    }

    @GetMapping("/api/v2/patients")
    public ResponseEntity<Response<Page<PatientDto>>> searchInfos(SearchCondition searchCondition, Pageable pageable) {
        Page<PatientDto> patients = patientService.getPatients(searchCondition, pageable);
        return ResponseEntity.ok().body(Response.success(patients));
    }

    @GetMapping("/api/v1/patient/{id}")
    public ResponseEntity<Response<PatientDto>> searchInfo(@PathVariable(name = "id") Long patientId) {
        return ResponseEntity.ok().body(Response.success(patientService.getPatient(patientId)));
    }

    @PutMapping("/api/v1/patient/{id}")
    public ResponseEntity<Response<PatientDto>> updateInfo(@PathVariable(name = "id") Long patientId, @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok().body(Response.success(patientService.updatePatient(patientId, patientDto)));
    }

    @DeleteMapping("/api/v1/patient/{id}")
    public ResponseEntity<Response<String>> deleteInfo(@PathVariable(name = "id") Long patientId) {
        patientService.removePatient(patientId);
        return ResponseEntity.ok().body(Response.success(Codes.D2000.desc));
    }
}
