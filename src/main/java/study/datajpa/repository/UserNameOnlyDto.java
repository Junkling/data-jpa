package study.datajpa.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserNameOnlyDto {
    private final String name;

    public UserNameOnlyDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
