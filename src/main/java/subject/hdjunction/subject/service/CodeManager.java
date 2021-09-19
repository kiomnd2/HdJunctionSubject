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
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
@Service
public class CodeManager {



    final private CodeRepository codeRepository;

    final private CodeGroupRepository codeGroupRepository;

    @PostConstruct
    public void initTransferData() {
        saveCodeGroup();
        saveCode();
    }

    @Transactional(readOnly = false)
    public void saveCodeGroup() {
        Resource codeGroupResource = new ClassPathResource("code_group.csv");

        try(BufferedReader br = new BufferedReader(
                Files.newBufferedReader(Paths.get(codeGroupResource.getURI())))) {
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

    private void saveCode() {
        Resource codeGroupResource = new ClassPathResource("code.csv");

        try(BufferedReader br = new BufferedReader(
                Files.newBufferedReader(Paths.get(codeGroupResource.getURI())))) {
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

    public String getCodeName(String group, String code) {
        Code code1 = codeRepository.findById(group, code).orElseThrow(NotFoundCodeException::new);
        return code1.getCodeName();
    }

}
