package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorMeasurementRT;
import me.stst.weatherstation.domain.SensorValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.List;

public interface SensorMeasurementDAO extends JpaRepository<SensorMeasurement,SensorMeasurement.SensorMeasurementId> {
    @Query("select sm from SensorMeasurement sm where sm.sensorValue=:sensorValue and sm.datetime=" +
            "(select max(sm2.datetime) from SensorMeasurement sm2 where sm.sensorValue=sm2.sensorValue)")
    List<SensorMeasurement> findLatestBySensorValue(@Param("sensorValue") SensorValue value);

    List<SensorMeasurement> findAllBySensorValue(SensorValue value);
}
