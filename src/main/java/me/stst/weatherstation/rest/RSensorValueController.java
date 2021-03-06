package me.stst.weatherstation.rest;

import me.stst.weatherstation.domain.Sensor;
import me.stst.weatherstation.domain.SensorValue;
import me.stst.weatherstation.repository.SensorDAO;
import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorMeasurementRTDAO;
import me.stst.weatherstation.repository.SensorValueDAO;
import me.stst.weatherstation.rest.model.RestGenericResponse;
import me.stst.weatherstation.rest.model.RestSensorValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/sensor_value")
@Controller
public class RSensorValueController {

    @Autowired
    private SensorDAO sensorDAO;
    @Autowired
    private SensorValueDAO sensorValueDAO;
    @Autowired
    private SensorMeasurementDAO sensorMeasurementDAO;
    @Autowired
    private SensorMeasurementRTDAO sensorMeasurementRTDAO;

    @GetMapping("/{sensorId}")
    public ResponseEntity<RestGenericResponse<List<RestSensorValue>>> getAllBySensor(@PathVariable Integer sensorId){
        Optional<Sensor> sensor=sensorDAO.findById(sensorId);
        return sensor.map(value -> {
            List<SensorValue> sv=sensorValueDAO.findAllBySensor(value);
            List<RestSensorValue> sensorValues=new ArrayList<>(sv.size());
            for (SensorValue s:sv){
                RestSensorValue rsv=s.toRestSensorValue();
                rsv.setLatestMeasurement(s.getLatestMeasurement(sensorMeasurementDAO,sensorMeasurementRTDAO));
                sensorValues.add(rsv);
            }
            return ResponseEntity.ok(new RestGenericResponse<>(sensorValues));
        })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestGenericResponse<>("Sensor not found")));
    }
}
