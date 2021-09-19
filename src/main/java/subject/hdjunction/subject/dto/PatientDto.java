package subject.hdjunction.subject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@ToString
@Getter
public class PatientDto {

    final private Long id;

    final private Long hospitalId;

    final private String patientName;

    final private String patientNo;

    final private String genderCode;

    final private String birthDate;

    final private String phoneNumber;

    private List<VisitDto> visitDtos;

    private LocalDateTime lastReceptionDateTime;

    public void addVisitDtos(List<VisitDto> visitDtos) {
        this.visitDtos = new ArrayList<>();
        this.visitDtos.addAll(visitDtos);
    }

    public void setLastReceptionDateTime(LocalDateTime lastReceptionDateTime) {
        this.lastReceptionDateTime = lastReceptionDateTime;
    }

    @Builder
    public PatientDto(Long id, Long hospitalId, String patientName, String patientNo, String genderCode,
                      String birthDate, String phoneNumber) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.patientName = patientName;
        this.patientNo = patientNo;
        this.genderCode = genderCode;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
