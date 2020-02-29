package me.stst.weatherstation.rest;

import me.stst.weatherstation.domain.Device;
import me.stst.weatherstation.domain.Sensor;
import me.stst.weatherstation.repository.DeviceDAO;
import me.stst.weatherstation.repository.SensorDAO;
import me.stst.weatherstation.rest.model.RestGenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/sensor")
@Controller
public class RSensorController {
    @Autowired
    private SensorDAO sensorDAO;
    @Autowired
    private DeviceDAO deviceDAO;

    @GetMapping("/{deviceId}")
    public ResponseEntity<RestGenericResponse<List<Sensor>>> getAllDevices(@PathVariable Integer deviceId){
        Optional<Device> device=deviceDAO.findById(deviceId);
        return device.map(value -> ResponseEntity.ok(new RestGenericResponse<>(sensorDAO.findAllByDevice(value)))).
                orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestGenericResponse<>("Device not found")));

    }
}
