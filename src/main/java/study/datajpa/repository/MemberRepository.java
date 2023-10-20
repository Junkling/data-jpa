package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

    @Query(name = "Member.findAllByName")
        //생략 가능
    List<Member> findAllByName(@Param("name") String name);

    @Query("select m from Member m where m.name = :name and m.age = :age")
        //로딩 시점에 파싱되기 때문에 문법 오류를 잡아 줌
    List<Member> findMemberByQuery(@Param("name") String name, @Param("age") int age);

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.name, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.name in :names")
    List<Member> findByNames(@Param("names") List<String> names);


    List<Member> findListByName(String name);

    Member findMemberByName(String name);

    Member findOptionalByName(String name);

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.name) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByName(@Param("name") String name);

    @QueryHints(value = @QueryHint(name = "org.hibernate.redOnly", value = "true"))
    Member findReadOnlyByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByName(String name);

}
