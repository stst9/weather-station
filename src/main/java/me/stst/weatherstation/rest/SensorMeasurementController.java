package me.stst.weatherstation.rest;

import me.stst.weatherstation.MainStorage;
import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorValue;
import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorValueDAO;
import me.stst.weatherstation.rest.model.RestSensorMeasurement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.TimeZone;

@Path("sensor_measurement")
public class SensorMeasurementController {
    private SessionFactory sessionFactory= MainStorage.getInstance().getSessionFactory();

    @GET
    public Response getBySensorValue(){
        return Response.ok().build();
    }

    @POST
    public Response newSensorValue(RestSensorMeasurement in){
        Response ret= Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        Session em=sessionFactory.openSession();
        Transaction transaction=em.beginTransaction();
        try {
            SensorValueDAO valueDAO=new SensorValueDAO(em);
            SensorValue sensorValue=valueDAO.findById(in.getSensorValueId());
            if (sensorValue!=null){
                SensorMeasurement sensorMeasurement=new SensorMeasurement();
                sensorMeasurement.setSensorValue(sensorValue);
                sensorMeasurement.setDatetime(new Date(in.getTimestamp()));
                sensorMeasurement.setValue(in.getValue());
                sensorMeasurement.setTimezone(TimeZone.getDefault().getRawOffset()/60000);
                em.persist(sensorMeasurement);
                em.getTransaction().commit();
                ret= Response.ok().build();
            }else {
                ret= Response.status(404).build();
            }
        }finally {
            try {
                em.getTransaction().rollback();
            }catch (Exception e){}
            try {
                em.close();
            }catch (Exception e){}
        }
        return ret;

    }
}
