package subject.hdjunction.subject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CodeGroup {

    @Id
    private String codeGroup;

    private String codeGroupName;

    private String description;

    @Builder
    public CodeGroup(String codeGroup, String codeGroupName, String description) {
        this.codeGroup = codeGroup;
        this.codeGroupName = codeGroupName;
        this.description = description;
    }
}
