package study.datajpa.repository;

public interface NestedClosedProjections {
    String getName();

    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
