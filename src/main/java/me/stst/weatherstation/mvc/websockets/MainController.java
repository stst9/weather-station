package me.stst.weatherstation.mvc.websockets;

import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorMeasurementRT;
import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorMeasurementRTDAO;
import me.stst.weatherstation.repository.SensorValueDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MainController {
    @Autowired
    private SensorValueDAO sensorValueDAO;
    @Autowired
    private SensorMeasurementRTDAO sensorMeasurementRTDAO;
    @Autowired
    private SensorMeasurementDAO sensorMeasurementDAO;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private  SimpMessageSendingOperations messagingTemplate;



    //@MessageMapping("/sensor_value")
    //@SendTo("/topic/sensor_value")
    public void getSensorValueRT(String id, Principal principal){
        this.template.convertAndSendToUser(principal.getName(),"/topic/sensor_value",
                sensorValueDAO.findById(Integer.parseInt(id)).get().getLatestMeasurement(sensorMeasurementDAO,sensorMeasurementRTDAO));
        //return sensorValueDAO.findById(Integer.parseInt(id)).get().getLatestMeasurement(sensorMeasurementDAO,sensorMeasurementRTDAO);
    }

    public void sendUpdate(SensorMeasurement measurement) {
        /*System.out.println("Fire");
        this.template.convertAndSend("/topic/sensor_value", measurement);
        this.template.convertAndSend("/queue/sensor_value", measurement);
        this.template.convertAndSend("/topic/sensor_value", "abc");
        this.template.convertAndSend("/queue/sensor_value", "def");

        System.out.println("Fired");*/
    }
}
