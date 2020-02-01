package me.stst.weatherstation.rest;

import me.stst.weatherstation.MainStorage;
import me.stst.weatherstation.domain.ApiToken;
import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorValue;
import me.stst.weatherstation.repository.ApiTokenDAO;
import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorValueDAO;
import me.stst.weatherstation.rest.model.RestSensorMeasurement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Path("sensor_measurement")
@Controller
@Secured
public class SensorMeasurementController {
    //private SessionFactory sessionFactory= MainStorage.getInstance().getSessionFactory();
    @Autowired
    private SensorValueDAO sensorValueDAO;
    @Autowired
    private SensorMeasurementDAO sensorMeasurementDAO;
    @Autowired
    private ApiTokenDAO apiTokenDAO;
    @GET
    public Response getBySensorValue(){
        return Response.ok().build();
    }

    @POST
    @Transactional
    public Response newSensorValue(RestSensorMeasurement in,
                                   @Context SecurityContext securityContext){
        Response ret= Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        ApiToken token=apiTokenDAO.findByToken(securityContext.getUserPrincipal().getName());
        try {
            Optional<SensorValue> sensorValueOpt=sensorValueDAO.findById(in.getSensorValueId());
            if (sensorValueOpt.isPresent()&&token.getDevice().getId()==sensorValueOpt.get().getSensor().getDevice().getId()){
                SensorMeasurement sensorMeasurement=new SensorMeasurement();
                sensorMeasurement.setSensorValue(sensorValueOpt.get());
                sensorMeasurement.setDatetime(new Date(in.getTimestamp()));
                sensorMeasurement.setValue(in.getValue());
                sensorMeasurement.setTimezone(TimeZone.getDefault().getRawOffset()/60000);
                sensorMeasurementDAO.save(sensorMeasurement);
                ret= Response.ok().build();
            }else {
                ret= Response.status(404).build();
            }
        }finally {

        }
        return ret;

    }
}
