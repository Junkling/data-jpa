package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "age"})
@NamedQuery(name = "Member.findAllByName",
        query = "select m from Member m where m.name = :name")
//장점 : 파싱 시점에 쿼리문의 문법 오류를 확인해 줌

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private Integer age;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username, int age) {
        this(username, age, null);
    }

    public Member(String name, Integer age, Team team) {
        this.name = name;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String name) {
        this(name, 0);
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
