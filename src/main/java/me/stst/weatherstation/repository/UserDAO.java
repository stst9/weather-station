package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User,Integer> {

    User findByLogin(String login);
}
