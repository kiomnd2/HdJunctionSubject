package subject.hdjunction.subject.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class PatientNoGenerator {

    public String generate() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while ( sb.length() < 13) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }

}
