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

    @Id
    @JoinColumn(name = "cd_grp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CodeGroup group;

    @Id
    private String code;

    private String codeName;

    @Builder
    public Code(CodeGroup group, String code, String codeName) {
        this.group = group;
        this.code = code;
        this.codeName = codeName;
    }
}
