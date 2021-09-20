package subject.hdjunction.subject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@IdClass(CodeId.class)
public class Code {

    // 코드그룹
    @Id
    @JoinColumn(name = "cd_grp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CodeGroup group;

    // 코드
    @Id
    @Column(name = "cd", length = 10)
    private String code;

    // 코드명
    @Column(name = "cd_nm", length = 20)
    private String codeName;

    @Builder
    public Code(CodeGroup group, String code, String codeName) {
        this.group = group;
        this.code = code;
        this.codeName = codeName;
    }
}
