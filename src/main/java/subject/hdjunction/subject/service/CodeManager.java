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

    final private CodeGroupRepository codeGroupRepository;

    /**
     * 코드 데이터를 가져와 데이터베이스에 입력합니다
     */
    @PostConstruct
    public void initTransferData() {
        saveCodeGroup();
        saveCode();
    }

    /**
     * 코드 그룹 데이터를 입력합니다
     */
    @Transactional(readOnly = false)
    public void saveCodeGroup() {
        Resource codeGroupResource = new ClassPathResource("code_group.csv");

        try(BufferedReader br = new BufferedReader(
                Files.newBufferedReader(Paths.get(codeGroupResource.getURI()), StandardCharsets.UTF_8))) {
            final CSVReader reader = new CSVReaderBuilder(br).withSkipLines(1).build();
            String[] data;
            while((data = reader.readNext()) != null) {
                codeGroupRepository.save(CodeGroup.builder()
                        .codeGroup(data[0])
                        .codeGroupName(data[1])
                        .description(data[2])
                        .build());
            }
        } catch (IOException e) {
            throw new CodeInjectionException();
        }
    }

    /**
     * 코드 데이터를 입력 받습니다
     */
    private void saveCode() {
        Resource codeGroupResource = new ClassPathResource("code.csv");

        try(BufferedReader br = new BufferedReader(
                Files.newBufferedReader(Paths.get(codeGroupResource.getURI()), StandardCharsets.UTF_8))) {
            final CSVReader reader = new CSVReaderBuilder(br).withSkipLines(1).build();
            String[] data;
            while((data = reader.readNext()) != null) {
                CodeGroup codeGroup = codeGroupRepository.findById(data[0]).orElseThrow(NotFoundCodeGroupException::new);
                codeRepository.save(Code.builder()
                        .group(codeGroup)
                        .code(data[1])
                        .codeName(data[2])
                        .build());
            }
        } catch (IOException e) {
            throw new CodeInjectionException();
        }
    }

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
