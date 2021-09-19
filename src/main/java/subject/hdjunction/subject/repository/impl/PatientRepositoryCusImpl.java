package subject.hdjunction.subject.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import subject.hdjunction.subject.domain.Patient;
import subject.hdjunction.subject.domain.QCode;
import subject.hdjunction.subject.dto.PatientDto;
import subject.hdjunction.subject.repository.PatientRepositoryCus;
import subject.hdjunction.subject.repository.SearchCondition;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static subject.hdjunction.subject.domain.QCode.*;
import static subject.hdjunction.subject.domain.QPatient.patient;
import static subject.hdjunction.subject.domain.QVisit.visit;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientRepositoryCusImpl implements PatientRepositoryCus {

    final private JPAQueryFactory queryFactory;

    @Override
    public List<PatientDto> findBySearchCondition(SearchCondition searchCondition) {
        return getPatientDtoJPAQuery(searchCondition).fetch();
    }

    @Override
    public Page<PatientDto> findBySearchConditionAndPageable(SearchCondition searchCondition, Pageable pageable) {
        List<PatientDto> contents = getPatientDtoJPAQuery(searchCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Patient> count = queryFactory
                .select(patient)
                .from(patient)
                .where(
                        patientNameEq(searchCondition.getPatientName()),
                        patientNoEq(searchCondition.getPatientNo()),
                        birthDateEq(searchCondition.getBirthDate()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(contents, pageable, count::fetchCount);
    }

    private JPAQuery<PatientDto> getPatientDtoJPAQuery(SearchCondition searchCondition) {
        return queryFactory
                .select(Projections.fields(
                        PatientDto.class,
                        patient.id,
                        patient.hospital().id.as("hospitalId"),
                        patient.patientName,
                        patient.patientNo,
                        patient.genderCode,
                        code1.codeName,
                        patient.birthDate,
                        patient.phoneNumber,
                        visit.receptionDateTime.max().as("lastReceptionDateTime")
                ))
                .from(patient)
                .leftJoin(patient.visits, visit)
                .leftJoin(code1)
                .on(code1.code.eq(patient.genderCode))
                .where(
                        patientNameEq(searchCondition.getPatientName()),
                        patientNoEq(searchCondition.getPatientNo()),
                        birthDateEq(searchCondition.getBirthDate()))
                .groupBy(patient);
    }

    private BooleanExpression patientNameEq(String patientName) {
        return hasText(patientName) ? patient.patientName.eq(patientName) : null;
    }

    private BooleanExpression patientNoEq(String patientNo) {
        return hasText(patientNo) ? patient.patientNo.eq(patientNo) : null;
    }

    private BooleanExpression birthDateEq(String birthDate) {
        return hasText(birthDate) ? patient.birthDate.eq(birthDate) : null;
    }


}
