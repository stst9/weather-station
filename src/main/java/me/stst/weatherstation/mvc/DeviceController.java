package me.stst.weatherstation.mvc;

import me.stst.weatherstation.domain.Device;
import me.stst.weatherstation.repository.DeviceDAO;
import me.stst.weatherstation.repository.SensorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/device/")
public class DeviceController {
    @Autowired
    private DeviceDAO deviceDAO;

    @Autowired
    private SensorDAO sensorDAO;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("devices",deviceDAO.findAll());
        return "/device/index";
    }

    @RequestMapping("/{id}")
    public String show(Model model, @PathVariable String id){
        Optional<Device> deviceOptional=deviceDAO.findById(Integer.parseInt(id));
        if (deviceOptional.isPresent()){
            Device device=deviceOptional.get();
            model.addAttribute("sensors", sensorDAO.findAllByDevice(device));
            model.addAttribute("device",device);
        }
        return "/device/show";
    }
}
