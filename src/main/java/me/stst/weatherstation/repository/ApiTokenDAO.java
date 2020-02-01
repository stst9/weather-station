package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.ApiToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiTokenDAO extends JpaRepository<ApiToken,Integer> {

    ApiToken findByToken(String token);
}
