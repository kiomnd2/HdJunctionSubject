package subject.hdjunction.subject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Hospital {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원명
    @Column(name = "hsptl_nm")
    private String hospitalName;

    // 요양기관번호
    @Column(name = "nrs_hm_no")
    private String nursingHomeNo;

    // 병원장명
    @Column(name = "chf_nm")
    private String ChiefName;

    @Builder
    public Hospital(String hospitalName, String nursingHomeNo, String chiefName) {
        this.hospitalName = hospitalName;
        this.nursingHomeNo = nursingHomeNo;
        this.ChiefName = chiefName;
    }
}
