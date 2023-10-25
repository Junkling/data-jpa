package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UserNameOnly {
    @Value("#{target.name + ' ' + target.age}")
    String getName();
}
