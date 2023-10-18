package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

    @Query(name = "Member.findAllByName") //생략 가능
    List<Member> findAllByName(@Param("name") String name);

    @Query("select m from Member m where m.name = :name and m.age = :age")
    //로딩 시점에 파싱되기 때문에 문법 오류를 잡아 줌
    List<Member> findMemberByQuery(@Param("name") String name, @Param("age") int age);
}