package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.Sensor;
import me.stst.weatherstation.domain.SensorValue;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public interface SensorValueDAO extends JpaRepository<SensorValue,Integer> {

    @Override
    Optional<SensorValue> findById(Integer integer);

    SensorValue findByName(String name);

    List<SensorValue> findAllBySensor(Sensor sensor);
}
