package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public void delete(Member member) {
        entityManager.remove(member);
    }

    public List<Member> findAll() {
        return entityManager.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = entityManager.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return entityManager.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }

    public List<Member> findByUserNameAndAgeGreaterThen(String name, Integer age) {
        return entityManager.createQuery("select m from Member m where m.name = :name and m.age> :age").setParameter("name", name).setParameter("age", age).getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit) {
        return entityManager.createQuery("select m from Member m where m.age = :age order by m.name desc")
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    public long totalCount(int age) {
        return entityManager.createQuery("select count(m) from Member m where m.age = :age",Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age) {
        return entityManager.createQuery("update Member m set m.age = m.age+1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }

}
