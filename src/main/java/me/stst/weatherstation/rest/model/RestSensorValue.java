package me.stst.weatherstation.rest.model;

import me.stst.weatherstation.domain.Sensor;
import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorMeasurementRT;
import me.stst.weatherstation.domain.Unit;
import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorMeasurementRTDAO;

import java.util.List;

public class RestSensorValue {

    private int id;
    private String name;
    private Unit unit;
    private Sensor sensor;
    private SensorMeasurement latestMeasurement;

    public RestSensorValue() {
    }

    public RestSensorValue(int id,String name, Unit unit, Sensor sensor) {
        this.id=id;
        this.name=name;
        this.unit=unit;
        this.sensor=sensor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public SensorMeasurement getLatestMeasurement() {
        return latestMeasurement;
    }

    public void setLatestMeasurement(SensorMeasurement latestMeasurement) {
        this.latestMeasurement = latestMeasurement;
    }
}
