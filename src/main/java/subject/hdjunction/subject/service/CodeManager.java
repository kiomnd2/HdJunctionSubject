package subject.hdjunction.subject.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subject.hdjunction.subject.domain.Code;
import subject.hdjunction.subject.domain.CodeGroup;
import subject.hdjunction.subject.exception.CodeInjectionException;
import subject.hdjunction.subject.exception.NotFoundCodeException;
import subject.hdjunction.subject.exception.NotFoundCodeGroupException;
import subject.hdjunction.subject.repository.CodeGroupRepository;
import subject.hdjunction.subject.repository.CodeRepository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
@Service
public class CodeManager {

    final private CodeRepository codeRepository;

    /**
     * 코드를 코드명으로 변환하여 출력합니다
     * @param group 코드그룹
     * @param code 코드키값
     * @return 코드명
     */
    public String getCodeName(String group, String code) {
        Code code1 = codeRepository.findById(group, code).orElseThrow(NotFoundCodeException::new);
        return code1.getCodeName();
    }

}
