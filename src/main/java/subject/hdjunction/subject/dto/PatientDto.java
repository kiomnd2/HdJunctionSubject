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

    // 환자 아이디
    final private Long id;

    // 병원 아이디
    final private Long hospitalId;

    // 환자명
    final private String patientName;

    // 환자 등록 번호
    final private String patientNo;

    // 성별 코드
    final private String genderCode;

    // 성별 명
    final private String genderName;

    // 생년월일
    final private String birthDate;

    // 휴대전화번호
    final private String phoneNumber;

    // 방문 리스트
    private List<VisitDto> visits;

    // 최근 방문 일자
    private LocalDateTime lastReceptionDateTime;

    public void addVisits(List<VisitDto> visits) {
        if (this.visits == null) this.visits = new ArrayList<>();
        this.visits.addAll(visits);
    }

    public void setLastReceptionDateTime(LocalDateTime lastReceptionDateTime) {
        this.lastReceptionDateTime = lastReceptionDateTime;
    }

    @Builder
    public PatientDto(Long id, Long hospitalId, String patientName, String patientNo, String genderCode,
                      String genderName, String birthDate, String phoneNumber) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.patientName = patientName;
        this.patientNo = patientNo;
        this.genderCode = genderCode;
        this.genderName = genderName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
