package subject.hdjunction.subject.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PatientNoGeneratorTest {

    @Test
    void patientNoGenerateTest() {
        String patientNo = PatientNoGenerator.generate();

        Assertions.assertThat(patientNo.length()).isEqualTo(13);
    }

}
