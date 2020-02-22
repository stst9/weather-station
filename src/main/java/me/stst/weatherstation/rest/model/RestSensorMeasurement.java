package me.stst.weatherstation.rest.model;

import me.stst.weatherstation.domain.SensorMeasurement;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
public class RestSensorMeasurement {

    private long timestamp;
    private BigDecimal value;
    private int sensorValueId;

    public RestSensorMeasurement() {
    }

    public RestSensorMeasurement(SensorMeasurement sensorMeasurement) {
        this.timestamp=sensorMeasurement.getDatetime().getTime();
        this.value=sensorMeasurement.getValue();
        this.sensorValueId=sensorMeasurement.getSensorValue().getId();
    }

    public int getSensorValueId() {
        return sensorValueId;
    }

    public void setSensorValueId(int sensorValueId) {
        this.sensorValueId = sensorValueId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
