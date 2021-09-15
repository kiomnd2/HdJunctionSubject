package subject.hdjunction.subject.domain;

// 환자방문

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Visit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hsptl_id")
    private Hospital hospital;

    // 환자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ptnt_id")
    private Patient patient;

    // 접수일시

    @Column(name = "rcptn_dt")
    private LocalDateTime receptionDateTime;

    // 방문상태코드
    @Column(name = "vst_st_cd")
    private String visitStateCode;

    @Builder
    public Visit(Hospital hospital, Patient patient, LocalDateTime receptionDateTime, String visitStateCode) {
        this.hospital = hospital;
        this.patient = patient;
        this.receptionDateTime = receptionDateTime;
        this.visitStateCode = visitStateCode;
    }
}
