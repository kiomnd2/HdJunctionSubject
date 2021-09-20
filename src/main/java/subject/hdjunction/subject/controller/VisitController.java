package subject.hdjunction.subject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subject.hdjunction.subject.codes.Codes;
import subject.hdjunction.subject.controller.response.Response;
import subject.hdjunction.subject.dto.VisitDto;
import subject.hdjunction.subject.service.VisitService;

@RestController
@RequiredArgsConstructor
public class VisitController {

    final private VisitService visitService;


    /**
     * 환자 방문 접수  정보를 등록합니다
     * @param visitDto 환자, 병원 정보를 포함한 방문 접수 정보
     * @return 방문정보
     */
    @PostMapping("/api/v1/visit")
    public ResponseEntity<Response<VisitDto>> register(@RequestBody VisitDto visitDto) {
        VisitDto visit = visitService.registerVisit(visitDto);
        return ResponseEntity.ok().body(Response.success(visit));
    }

    /**
     * 방문 접수 정보를 조회합니다.
     * @param visitId 방문 접수 아이디
     * @return 방문 접수 정보
     */
    @GetMapping("/api/v1/visit/{id}")
    public ResponseEntity<Response<VisitDto>> searchInfo(@PathVariable(name = "id") Long visitId) {
        return ResponseEntity.ok().body(Response.success(visitService.getVisit(visitId)));
    }

    /**
     * 방문 접수 정보를 수정합니다.
     * @param visitId 방문 접수 아이디
     * @param visitDto 수정할 방문 접수 정보
     * @return 방문 접수 정보
     */
    @PutMapping("/api/v1/visit/{id}")
    public ResponseEntity<Response<VisitDto>> updateInfo(@PathVariable(name = "id") Long visitId, @RequestBody VisitDto visitDto) {
        return ResponseEntity.ok().body(Response.success(visitService.updateVisit(visitId, visitDto)));
    }

    /**
     * 방문 접수 정보를 제거합니다.
     * @param visitId 방문 접수 아이디
     * @return 제거 상태
     */
    @DeleteMapping("/api/v1/visit/{id}")
    public ResponseEntity<Response<String>> deleteInfo(@PathVariable(name = "id") Long visitId) {
        visitService.removeVisit(visitId);
        return ResponseEntity.ok().body(Response.success(Codes.D2000.desc));
    }
}
