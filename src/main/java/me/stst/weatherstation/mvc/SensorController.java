package me.stst.weatherstation.mvc;

import me.stst.weatherstation.domain.Sensor;
import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorMeasurementRT;
import me.stst.weatherstation.domain.SensorValue;
import me.stst.weatherstation.repository.SensorDAO;
import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorMeasurementRTDAO;
import me.stst.weatherstation.repository.SensorValueDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/sensor/")
public class SensorController {
    @Autowired
    private SensorDAO sensorDAO;
    @Autowired
    private SensorValueDAO sensorValueDAO;
    @Autowired
    private SensorMeasurementRTDAO sensorMeasurementRTDAO;
    @Autowired
    private SensorMeasurementDAO sensorMeasurementDAO;
    @RequestMapping("/{id}")
    public String show(Model model, @PathVariable String id){
        Optional<Sensor> sensorOptional=sensorDAO.findById(Integer.parseInt(id));
        if (sensorOptional.isPresent()){
            model.addAttribute("sensor",sensorOptional.get());
            List<SensorValue> sensorValues=sensorValueDAO.findAllBySensor(sensorOptional.get());
            model.addAttribute("sensor_values",sensorValues);
            Map<Integer, SensorMeasurement> latestValues=new HashMap<>();
            for (SensorValue value:sensorValues){
                latestValues.put(value.getId(),value.getLatestMeasurement(sensorMeasurementDAO,sensorMeasurementRTDAO));
            }
            model.addAttribute("latest_measurements",latestValues);
        }
        return "/sensor/show";
    }
}
