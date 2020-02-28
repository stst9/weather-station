package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.ApiToken;
import me.stst.weatherstation.domain.Device;
import me.stst.weatherstation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiTokenDAO extends JpaRepository<ApiToken,Integer> {

    ApiToken findByToken(String token);

    List<ApiToken> findByUserAndDevice(User user, Device device);
}
