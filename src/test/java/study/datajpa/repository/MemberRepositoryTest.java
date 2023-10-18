package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Test
    public void test() {
        Member member = new Member("memberB");
        Member saved = memberRepository.save(member);
        Member findMember = memberRepository.findById(saved.getId()).orElseThrow();

        assertThat(saved.getId()).isEqualTo(findMember.getId());
        assertThat(saved.getName()).isEqualTo(findMember.getName());
        assertThat(saved).isEqualTo(findMember);
    }
    @Test
    public void CRUD1() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        Long count = memberRepository.count();
        assertThat(count).isEqualTo(0);
    }
    @Test
    public void findByNameAndAge() {
        Member aaa1 = new Member("AAA", 10);
        Member aaa2 = new Member("AAA", 20);
        memberRepository.save(aaa1);
        memberRepository.save(aaa2);

        List<Member> result = memberRepository.findByNameAndAgeGreaterThan("AAA", 15);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void namedQuery() {
        Member aaa1 = new Member("AAA", 10);
        Member aaa2 = new Member("AAA", 20);
        memberRepository.save(aaa1);
        memberRepository.save(aaa2);

        List<Member> result = memberRepository.findAllByName("AAA");
        assertThat(result.size()).isEqualTo(2);
    }@Test
    void testQuery() {
        Member aaa1 = new Member("AAA", 10);
        Member aaa2 = new Member("AAA", 20);
        memberRepository.save(aaa1);
        memberRepository.save(aaa2);

        List<Member> result = memberRepository.findMemberByQuery("AAA",10);
        assertThat(result.size()).isEqualTo(1);
    }

}