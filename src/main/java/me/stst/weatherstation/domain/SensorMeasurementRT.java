package me.stst.weatherstation.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "sensor_measurements_rt")
@IdClass(SensorMeasurementRT.SensorMeasurementId.class)
public class SensorMeasurementRT{

    @Id
    @ManyToOne
    @JoinColumn(name = "sv_id",nullable = false)
    private SensorValue sensorValue;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sm_datetime",nullable = false)
    private Date datetime;

    @Id
    @Column(name = "sm_timezone",nullable = false)
    private int timezone; //Timezone offset in minutes

    @Id
    @Column(name = "sm_value",precision = 12,scale = 4)
    private BigDecimal value;

    public SensorMeasurement copyToSensorMeasurement(){
        SensorMeasurement ret=new SensorMeasurement();
        ret.setTimezone(timezone);
        ret.setDatetime(datetime);
        ret.setValue(value);
        ret.setSensorValue(sensorValue);
        return ret;
    }

    public SensorValue getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(SensorValue sensorValue) {
        this.sensorValue = sensorValue;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public static class SensorMeasurementId implements Serializable {
        private int sensorValue;
        private Date datetime;
        private int timezone;
        private BigDecimal value;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SensorMeasurementRT.SensorMeasurementId that = (SensorMeasurementRT.SensorMeasurementId) o;
            return timezone == that.timezone &&
                    sensorValue==that.sensorValue &&
                    datetime.equals(that.datetime) &&
                    value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sensorValue, datetime, timezone, value);
        }
    }

}
