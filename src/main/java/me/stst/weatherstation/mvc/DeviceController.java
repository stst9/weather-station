package me.stst.weatherstation.mvc;

import me.stst.weatherstation.domain.Device;
import me.stst.weatherstation.domain.Sensor;
import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorValue;
import me.stst.weatherstation.repository.DeviceDAO;
import me.stst.weatherstation.repository.SensorDAO;
import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorMeasurementRTDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/device/")
public class DeviceController {
    @Autowired
    private DeviceDAO deviceDAO;

    @Autowired
    private SensorDAO sensorDAO;

    @Autowired
    private SensorMeasurementDAO sensorMeasurementDAO;

    @Autowired
    private SensorMeasurementRTDAO sensorMeasurementRTDAO;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("devices",deviceDAO.findAll());
        return "/device/index";
    }

    @RequestMapping("/{id}")
    @Transactional
    public String show(Model model, @PathVariable String id){
        Optional<Device> deviceOptional=deviceDAO.findById(Integer.parseInt(id));
        if (deviceOptional.isPresent()){
            Device device=deviceOptional.get();
            List<Sensor> sensors=sensorDAO.findAllByDevice(device);
            model.addAttribute("sensors", sensors);
            model.addAttribute("device",device);
            Map<Integer, SensorMeasurement> latestValues=new HashMap<>();
            for (Sensor sensor:sensors){
                for (SensorValue value:sensor.getSensorValues()){
                    latestValues.put(value.getId(),value.getLatestMeasurement(sensorMeasurementDAO,sensorMeasurementRTDAO));
                }
            }
            model.addAttribute("latest_measurements",latestValues);
        }
        return "/device/show";
    }
}
