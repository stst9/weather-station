package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.SensorMeasurement;

import javax.persistence.EntityManager;

public class SensorMeasurementDAO extends BaseDAO {

    public SensorMeasurementDAO(EntityManager em) {
        super(em);
    }

    @Override
    public Class getBaseClass() {
        return SensorMeasurement.class;
    }
}
