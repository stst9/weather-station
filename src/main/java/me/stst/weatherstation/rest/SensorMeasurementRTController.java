package me.stst.weatherstation.rest;

import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorMeasurementRT;
import me.stst.weatherstation.domain.SensorValue;
import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorMeasurementRTDAO;
import me.stst.weatherstation.repository.SensorValueDAO;
import me.stst.weatherstation.rest.model.RestSensorMeasurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Path("sensor_measurement_rt")
@Controller
public class SensorMeasurementRTController {

    @Autowired
    private SensorValueDAO sensorValueDAO;
    @Autowired
    private SensorMeasurementRTDAO sensorMeasurementRTDAO;
    @Autowired
    private SensorMeasurementDAO sensorMeasurementDAO;

    @POST
    @Transactional
    public Response newSensorValue(RestSensorMeasurement in){
        Response ret= Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        try {
            Optional<SensorValue> sensorValueOpt=sensorValueDAO.findById(in.getSensorValueId());
            if (sensorValueOpt.isPresent()){
                SensorMeasurementRT sensorMeasurement=new SensorMeasurementRT();
                sensorMeasurement.setSensorValue(sensorValueOpt.get());
                sensorMeasurement.setDatetime(new Date(in.getTimestamp()));
                sensorMeasurement.setValue(in.getValue());
                sensorMeasurement.setTimezone(TimeZone.getDefault().getRawOffset()/60000);
                sensorMeasurementRTDAO.save(sensorMeasurement);
                ret= Response.ok().build();
            }else {
                ret= Response.status(404).build();
            }
        }finally {

        }
        return ret;

    }

    @Async
    @Transactional
    @Scheduled(fixedRate = 1000)
    public void calculateAverages(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MINUTE,-1);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        Date start=calendar.getTime();
        calendar=Calendar.getInstance();
        calendar.add(Calendar.MINUTE,-1);
        calendar.set(Calendar.MILLISECOND,999);
        calendar.set(Calendar.SECOND,59);
        Date end=calendar.getTime();
        List<SensorValue> sensorValues=sensorValueDAO.findAll();
        for (SensorValue sensorValue:sensorValues){
            List<SensorMeasurementRT> measurementRTS=sensorMeasurementRTDAO.findAllBySensorValueAndDatetimeBetween(sensorValue,start,end);
            BigDecimal[] values =new BigDecimal[measurementRTS.size()];
            int i=0;
            for (SensorMeasurementRT measurementRT:measurementRTS){
                values[i]=measurementRT.getValue();
                i++;
            }
            if (values.length>0){
                Arrays.sort(values);
                BigDecimal median;
                if (values.length%2==0){
                    median=values[values.length/2 -1].add(values[values.length/2]).divide(BigDecimal.valueOf(2),4, RoundingMode.HALF_UP);
                }else {
                    median=values[(values.length-1)/2];
                }
                SensorMeasurement measurement=new SensorMeasurement();
                measurement.setSensorValue(sensorValue);
                measurement.setValue(median);
                measurement.setDatetime(start);
                measurement.setTimezone(measurementRTS.get(0).getTimezone());
                sensorMeasurementDAO.save(measurement);
            }
        }
        sensorMeasurementRTDAO.deleteAllByDatetimeBefore(start);
    }
}
