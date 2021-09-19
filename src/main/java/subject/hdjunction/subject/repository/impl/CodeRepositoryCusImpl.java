package subject.hdjunction.subject.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import subject.hdjunction.subject.domain.Code;
import subject.hdjunction.subject.repository.CodeRepositoryCus;

import java.util.Optional;

import static subject.hdjunction.subject.domain.QCode.code1;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeRepositoryCusImpl implements CodeRepositoryCus {

    final private JPAQueryFactory queryFactory;

    @Override
    public Optional<Code> findById(String group, String id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(code1)
                .where(code1.group().codeGroup.eq(group)
                        .and(code1.code.eq(id)))
                .fetchOne());
    }
}
