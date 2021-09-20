package subject.hdjunction.subject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@ToString
@Getter
public class VisitDto {

    // 방문 아이디
    final private Long id;

    // 병원 아이디
    final private Long hospitalId;

    // 환자 아아디
    final private Long patientId;

    // 접수일자
    final private LocalDateTime receptionDateTime;

    // 방문상태코드
    final private String visitStateCode;

    @Builder
    public VisitDto(Long id, Long hospitalId, Long patientId, LocalDateTime receptionDateTime, String visitStateCode) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.patientId = patientId;
        this.receptionDateTime = receptionDateTime;
        this.visitStateCode = visitStateCode;
    }
}
