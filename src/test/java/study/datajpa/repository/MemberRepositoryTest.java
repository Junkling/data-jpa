package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

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

    @Test
    void findMemberDto() {
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);
        Team teamA = new Team("teamA");
        m1.setTeam(teamA);
        teamRepository.save(teamA);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("DTO = "+dto.toString());
        }
    }

    @Test
    void findByNames() {
        Member aaa1 = new Member("AAA", 10);
        Member aaa2 = new Member("BBB", 20);
        memberRepository.save(aaa1);
        memberRepository.save(aaa2);
        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member byName : byNames) {
            System.out.println(byName);
        }
    }

    @Test
    void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        memberRepository.findMemberByName("aadsasf");
        //NoResultException을 try/catch 하여 null 반환
        memberRepository.findOptionalByName("aadsasf");
        //null값에 대한 처리를 사용자에게 맡김
        memberRepository.findOptionalByName("AAA");
        //NoUniqueException 발생

        List<Member> resultList = memberRepository.findListByName("aadsasf");
        System.out.println(resultList.size());
    }
    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        int age = 10;
        PageRequest pageReq = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        Page<Member> byAge = memberRepository.findByAge(age, pageReq);
        Page<MemberDto> toDto = byAge.map(member -> new MemberDto(member.getId(), member.getName(), null));

        List<Member> content = byAge.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(byAge.getTotalElements()).isEqualTo(6);
        assertThat(byAge.getNumber()).isEqualTo(0);
        assertThat(byAge.getTotalPages()).isEqualTo(2);
        assertThat(byAge.isFirst()).isTrue();
        assertThat(byAge.hasNext()).isTrue();

        for (Member member : content) {
            System.out.println("member" + member);
        }

//        Slice<Member> byAgeBySlice = memberRepository.findByAge(age, pageReq);
//        List<Member> contentBySlice = byAgeBySlice.getContent();
//
//        assertThat(contentBySlice.size()).isEqualTo(3);
//        assertThat(byAgeBySlice.getNumber()).isEqualTo(0);
//        assertThat(byAgeBySlice.isFirst()).isTrue();
//        assertThat(byAgeBySlice.hasNext()).isTrue();
//
//         for (Member member : contentBySlice) {
//            System.out.println("member" + member);
//        }
    }
    @Test
    @Rollback(value = false)
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 19));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        int resultCount = memberRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(3);
//        em.clear();
        Member member5 = memberRepository.findMemberByName("member5");
        System.out.println(member5);
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

//        N+1 문제를 해결하려면 @Query로 직접 fetch join을 하거나 @EntityGraph 사용 (상위 dataJPA에서 제공하는 메서드의 경우 override)
        List<Member> all = memberRepository.findAll();
        for (Member member : all) {
            System.out.println("member = " + member.getName());
            System.out.println("member.team = " + member.getTeam().getName());
        }
//
//        List<Member> allFetch = memberRepository.findMemberFetchJoin();
//        for (Member member : allFetch) {
//            System.out.println("member = " + member.getName());
//            System.out.println("member.team = " + member.getTeam().getName());
//        }
    }

    @Test
    public void queryHint() {
        memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

//        Member findMember = memberRepository.findMemberByName("member1");
        //자동 변경 감지
//        findMember.setName("member2");

        //queryHint 를 사용하여 hibernate의 설정을 지정하여 쿼리를 날릴 수 있다
        Member findMemberReadOnly = memberRepository.findReadOnlyByName("member1");
        findMemberReadOnly.setName("member2");
        em.flush();
    }

    @Test
    public void lock() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByName("member1");
    }

    @Test
    public void callCustom() {
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @Test
    public void projections() {
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

//        List<UserNameOnlyDto> result = memberRepository.findProjectionsByName("m1");
        List<NestedClosedProjections> result = memberRepository.findProjections2ByName("m1",NestedClosedProjections.class);
//        for (UserNameOnlyDto userNameOnly : result) {
//            System.out.println(userNameOnly.getName());
//        }
        for (NestedClosedProjections nestedClosedProjections : result) {
            System.out.println(nestedClosedProjections.getName()+nestedClosedProjections.getTeam().getName());
        }
    }

    @Test
    public void nativeQuery() {
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Member result = memberRepository.findByNativeQuery("m1");
        Page<MemberProjection> result2 = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        System.out.println(result.toString());

        List<MemberProjection> content = result2.getContent();
        for (MemberProjection memberProjection : content) {
            System.out.println(memberProjection.getName());
            System.out.println(memberProjection.getTeamName());
        }
    }


}