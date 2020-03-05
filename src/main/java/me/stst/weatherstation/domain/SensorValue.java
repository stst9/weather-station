package me.stst.weatherstation.domain;

import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorMeasurementRTDAO;
import me.stst.weatherstation.rest.model.RestSensorValue;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sensor_values", uniqueConstraints = {@UniqueConstraint(columnNames = {"sv_name","s_id"})})
public class SensorValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sv_id", nullable = false)
    private int id;

    @Column(name = "sv_name", nullable = false)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "un_id")
    private Unit unit;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "s_id", nullable = false)
    private Sensor sensor;

    public SensorValue() {
    }

    public SensorValue(String name,Unit unit,Sensor sensor) {
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

    public SensorMeasurement getLatestMeasurement( SensorMeasurementDAO sensorMeasurementDAO, SensorMeasurementRTDAO sensorMeasurementRTDAO){
        List<SensorMeasurementRT> sensorMeasurementRT=sensorMeasurementRTDAO.findLatestBySensorValue(this);
        if (sensorMeasurementRT.size()==0){
            List<SensorMeasurement> measurement=sensorMeasurementDAO.findLatestBySensorValue(this);
            if (measurement.size()>0){
                return measurement.get(0);
            }
        }else{
            return sensorMeasurementRT.get(0).copyToSensorMeasurement();
        }
        return null;
    }

    public RestSensorValue toRestSensorValue(){
        return new RestSensorValue(id,name,unit,sensor);
    }
}
