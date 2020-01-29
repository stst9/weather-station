package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.Device;
import me.stst.weatherstation.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorDAO extends JpaRepository<Sensor,Integer> {

    List<Sensor> findAllByDevice(Device device);
}
