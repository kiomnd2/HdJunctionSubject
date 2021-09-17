package subject.hdjunction.subject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    final private List<VisitDto> visitDtos = new ArrayList<>();

    private LocalDateTime lastReceptionDateTime;

    public void addVisitDtos(List<VisitDto> visitDtos) {
        this.visitDtos.addAll(visitDtos);
    }

    public void setListReceptionDateTime(LocalDateTime lastReceptionDateTime) {
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
