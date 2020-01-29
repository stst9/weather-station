package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceDAO extends JpaRepository<Device,Integer> {
}
