package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    // 상위 인터페이스를 상속한 인터페이스의 이름 뒤에 Impl을 붙힌 클래스 명으로 사용해야 한다 (JPA에서 자동으로 찾아서 매칭함)
    private final EntityManager em;


    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
