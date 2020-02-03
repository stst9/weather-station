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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@RestController()
@RequestMapping("/rest/sensor_measurement")
@Controller
public class SensorMeasurementController {
    //private SessionFactory sessionFactory= MainStorage.getInstance().getSessionFactory();
    @Autowired
    private SensorValueDAO sensorValueDAO;
    @Autowired
    private SensorMeasurementDAO sensorMeasurementDAO;
    @Autowired
    private ApiTokenDAO apiTokenDAO;
    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping
    @Transactional
    public ResponseEntity newSensorValue(@RequestBody RestSensorMeasurement in,
                                         Principal principal){
        ResponseEntity ret= ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        ApiToken token=apiTokenDAO.findByToken(principal.getName());
        try {
            Optional<SensorValue> sensorValueOpt=sensorValueDAO.findById(in.getSensorValueId());
            if (sensorValueOpt.isPresent()&&token.getDevice().getId()==sensorValueOpt.get().getSensor().getDevice().getId()){
                SensorMeasurement sensorMeasurement=new SensorMeasurement();
                sensorMeasurement.setSensorValue(sensorValueOpt.get());
                sensorMeasurement.setDatetime(new Date(in.getTimestamp()));
                sensorMeasurement.setValue(in.getValue());
                sensorMeasurement.setTimezone(TimeZone.getDefault().getRawOffset()/60000);
                sensorMeasurementDAO.save(sensorMeasurement);
                ret= ResponseEntity.ok().build();
                template.convertAndSend("/topic/sensor_value/"+sensorValueOpt.get().getId(),in);
            }else {
                ret= ResponseEntity.status(404).build();
            }
        }finally {

        }
        return ret;

    }
}
