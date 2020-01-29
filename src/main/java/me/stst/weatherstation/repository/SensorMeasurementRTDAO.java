package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.SensorMeasurementRT;
import me.stst.weatherstation.domain.SensorValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface SensorMeasurementRTDAO extends JpaRepository<SensorMeasurementRT,Integer> {

    SensorMeasurementRT findBySensorValue(SensorValue sensorValue);

    /*@Query("select smr from SensorMeasurementRT smr where (UNIX_TIMESTAMP(smr.datetime)/60)=:minute")
    List<SensorMeasurementRT> findAllLastMinute(@Param("start") long minute);*/

    List<SensorMeasurementRT> findAllBySensorValueAndDatetimeBetween(SensorValue sensorValue, Date start, Date end);

    @Query("select smr from SensorMeasurementRT smr where smr.sensorValue=:sensorValue and smr.datetime=" +
            "(select max(smr2.datetime) from SensorMeasurementRT smr2 where smr.sensorValue=smr2.sensorValue)")
    List<SensorMeasurementRT> findLatestBySensorValue(@Param("sensorValue") SensorValue value);
    @Modifying
    @Transactional
    @Query("delete from SensorMeasurementRT smr where smr.datetime<:date")
    void deleteAllByDatetimeBefore(@Param("date") Date date);
}
