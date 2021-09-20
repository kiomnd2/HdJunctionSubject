package subject.hdjunction.subject.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subject.hdjunction.subject.dto.PatientDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Patient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hsptl_id")
    private Hospital hospital;

    // 환자명
    @Column(name = "ptnt_nm", length = 45)
    private String patientName;

    // 환자등록번호
    @Column(name = "ptnt_no", length = 13)
    private String patientNo;

    // 성별코드
    @Column(name = "gndr_cd", length = 10)
    private String genderCode;

    // 생년월일
    @Column(name = "brth_dt", length = 10)
    private String birthDate;

    // 핸드폰 번호
    @Column(name = "phn_no", length = 20)
    private String phoneNumber;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Visit> visits = new ArrayList<>();

    public Patient updatePatient(PatientDto patientDto) {
        this.patientName = patientDto.getPatientName();
        this.phoneNumber = patientDto.getPhoneNumber();
        this.birthDate = patientDto.getBirthDate();
        return this;
    }

    @Builder
    public Patient(Hospital hospital, String patientName, String patientNo, String genderCode, String birthDate
            , String phoneNumber, List<Visit> visits) {
        this.hospital = hospital;
        this.patientName = patientName;
        this.patientNo = patientNo;
        this.genderCode = genderCode;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.visits = visits;
    }
}
