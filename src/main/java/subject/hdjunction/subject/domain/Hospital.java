package subject.hdjunction.subject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Hospital {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원명
    @Column(name = "hsptl_nm", length = 45)
    private String hospitalName;

    // 요양기관번호
    @Column(name = "nrs_hm_no", length = 20)
    private String nursingHomeNo;

    // 병원장명
    @Column(name = "chf_nm", length = 10)
    private String ChiefName;

    @Builder
    public Hospital(String hospitalName, String nursingHomeNo, String chiefName) {
        this.hospitalName = hospitalName;
        this.nursingHomeNo = nursingHomeNo;
        this.ChiefName = chiefName;
    }
}
