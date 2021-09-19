package subject.hdjunction.subject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subject.hdjunction.subject.consts.CodeGroupConstant;


@SpringBootTest
class CodeManagerTest {


    @Autowired
    CodeManager codeManager;

    @Test
    void getCodeTest() throws Throwable {
        String codeGender = codeManager.getCodeName(CodeGroupConstant.GENDER, "M");
        Assertions.assertThat(codeGender).isEqualTo("남");

        String codeVisit = codeManager.getCodeName(CodeGroupConstant.VISIT, "1");
        Assertions.assertThat(codeVisit).isEqualTo("방문중");

        String treatSubject = codeManager.getCodeName(CodeGroupConstant.TREAT_SUBJECT, "01");
        Assertions.assertThat(treatSubject).isEqualTo("내과");

        String treatType = codeManager.getCodeName(CodeGroupConstant.TREAT_TYPE, "T");
        Assertions.assertThat(treatType).isEqualTo("검사");

    }

}
