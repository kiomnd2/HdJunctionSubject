package subject.hdjunction.subject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subject.hdjunction.subject.codes.Codes;
import subject.hdjunction.subject.controller.response.Response;
import subject.hdjunction.subject.dto.PatientDto;
import subject.hdjunction.subject.dto.VisitDto;
import subject.hdjunction.subject.service.VisitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VisitController {

    final private VisitService visitService;


    @PostMapping("/api/v1/visit")
    public ResponseEntity<Response<VisitDto>> register(@RequestBody VisitDto visitDto) {
        VisitDto visit = visitService.registerVisit(visitDto);
        return ResponseEntity.ok().body(Response.success(visit));
    }

    @GetMapping("/api/v1/visit/{id}")
    public ResponseEntity<Response<VisitDto>> searchInfo(@PathVariable(name = "id") Long visitId) {
        return ResponseEntity.ok().body(Response.success(visitService.getVisit(visitId)));
    }

    @PutMapping("/api/v1/visit/{id}")
    public ResponseEntity<Response<VisitDto>> updateInfo(@PathVariable(name = "id") Long visitId, VisitDto visitDto) {
        return ResponseEntity.ok().body(Response.success(visitService.updateVisit(visitId, visitDto)));
    }

    @DeleteMapping("/api/v1/visit/{id}")
    public ResponseEntity<Response<String>> deleteInfo(@PathVariable(name = "id") Long visitId) {
        visitService.removeVisit(visitId);
        return ResponseEntity.ok().body(Response.success(Codes.E4000.desc));
    }
}
