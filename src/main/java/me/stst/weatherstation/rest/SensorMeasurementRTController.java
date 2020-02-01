package me.stst.weatherstation.rest;

import me.stst.weatherstation.domain.ApiToken;
import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorMeasurementRT;
import me.stst.weatherstation.domain.SensorValue;
import me.stst.weatherstation.repository.ApiTokenDAO;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Path("sensor_measurement_rt")
@Controller
@Secured
public class SensorMeasurementRTController {

    @Autowired
    private SensorValueDAO sensorValueDAO;
    @Autowired
    private SensorMeasurementRTDAO sensorMeasurementRTDAO;
    @Autowired
    private SensorMeasurementDAO sensorMeasurementDAO;
    @Autowired
    private ApiTokenDAO apiTokenDAO;

    @POST
    @Transactional
    public Response newSensorValue(RestSensorMeasurement in,
                                   @Context SecurityContext securityContext){
        Response ret= Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        ApiToken token=apiTokenDAO.findByToken(securityContext.getUserPrincipal().getName());
        try {
            Optional<SensorValue> sensorValueOpt=sensorValueDAO.findById(in.getSensorValueId());
            if (sensorValueOpt.isPresent()&&token.getDevice().getId()==sensorValueOpt.get().getSensor().getDevice().getId()){
                SensorMeasurementRT sensorMeasurementRT=new SensorMeasurementRT();
                sensorMeasurementRT.setSensorValue(sensorValueOpt.get());
                sensorMeasurementRT.setDatetime(new Date(in.getTimestamp()));
                sensorMeasurementRT.setValue(in.getValue());
                sensorMeasurementRT.setTimezone(TimeZone.getDefault().getRawOffset()/60000);
                sensorMeasurementRTDAO.save(sensorMeasurementRT);
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
