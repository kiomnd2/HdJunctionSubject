package subject.hdjunction.subject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CodeGroup {

    // 코드그룹
    @Id
    @Column(name = "cd_grp", length = 20)
    private String codeGroup;

    // 코드그룹명
    @Column(name = "cd_grp_nm", length = 20)
    private String codeGroupName;

    // 코드 설명
    @Column(name = "desc", length = 500)
    private String description;

    @Builder
    public CodeGroup(String codeGroup, String codeGroupName, String description) {
        this.codeGroup = codeGroup;
        this.codeGroupName = codeGroupName;
        this.description = description;
    }
}
