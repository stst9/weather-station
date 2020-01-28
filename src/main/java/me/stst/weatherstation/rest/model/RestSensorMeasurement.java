package me.stst.weatherstation.rest.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
public class RestSensorMeasurement {

    long timestamp;
    BigDecimal value;
    int sensorValueId;

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
