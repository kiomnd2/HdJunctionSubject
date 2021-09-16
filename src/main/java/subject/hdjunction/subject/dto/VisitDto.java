package subject.hdjunction.subject.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VisitDto {

    final private Long id;

    final private Long hospitalId;

    final private Long patientId;

    final private LocalDateTime receptionDateTime;

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
