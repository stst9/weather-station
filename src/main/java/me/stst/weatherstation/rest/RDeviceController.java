package me.stst.weatherstation.rest;

import me.stst.weatherstation.domain.Device;
import me.stst.weatherstation.repository.DeviceDAO;
import me.stst.weatherstation.rest.model.RestGenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/device")
@Controller
public class RDeviceController {
    @Autowired
    private DeviceDAO deviceDAO;

    @GetMapping
    public ResponseEntity<RestGenericResponse<List<Device>>> getAllDevices(){
        return ResponseEntity.ok(new RestGenericResponse<>(deviceDAO.findAll()));
    }
}
