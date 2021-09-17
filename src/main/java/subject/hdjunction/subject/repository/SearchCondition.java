package subject.hdjunction.subject.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SearchCondition {

    @Setter
    private String patientName;

    @Setter
    private String patientNo;

    @Setter
    private String birthDate;
}
