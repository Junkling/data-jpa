package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;


    @Test
    @Transactional
//    @Rollback(value = false)
    public void testEntity() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();
        em.clear();

        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("Member = " + member);
            System.out.println(" -> member.getTeam = " + member.getTeam());
        }
    }

    @Test
    @Rollback(value = false)
    public void JpaEventBaseEntity() throws InterruptedException {
        Member member1 = new Member("member1");
        memberRepository.save(member1);

        Thread.sleep(100);
        em.flush();
        em.clear();

        Member member = memberRepository.findById(member1.getId()).orElseThrow();

        System.out.println(member.getCreateDate());
        System.out.println(member.getLastModifiedDate());
        System.out.println(member.getCreatedBy());
        System.out.println(member.getLastModifiedBy());
    }
}