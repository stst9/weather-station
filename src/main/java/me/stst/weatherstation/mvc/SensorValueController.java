package me.stst.weatherstation.mvc;

import com.google.gson.Gson;
import me.stst.weatherstation.domain.SensorMeasurement;
import me.stst.weatherstation.domain.SensorValue;
import me.stst.weatherstation.mvc.websockets.MainController;
import me.stst.weatherstation.repository.SensorMeasurementDAO;
import me.stst.weatherstation.repository.SensorValueDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sensor_value")
public class SensorValueController {
    @Autowired
    private MainController mainController;
    private Gson gson=new Gson();

    @Autowired
    private SensorValueDAO sensorValueDAO;
    @Autowired
    private SensorMeasurementDAO sensorMeasurementDAO;
    @RequestMapping("/{id}")
    public String show(Model model, @PathVariable String id){
        Optional<SensorValue> sensorValue=sensorValueDAO.findById(Integer.parseInt(id));
        if (sensorValue.isPresent()){
            model.addAttribute("sensor_value",sensorValue.get());
            List<SensorMeasurement> measurementList=sensorMeasurementDAO.findAllBySensorValue(sensorValue.get());
            model.addAttribute("sensor_measurements",measurementList);
            String labels[]=new String[measurementList.size()];
            String data[]=new String[measurementList.size()];
            int i=0;
            for (SensorMeasurement measurement:measurementList){
                labels[i]=measurement.getDatetime().toString();
                data[i]=measurement.getValue().toString();
                i++;
            }
            model.addAttribute("chart_labels",gson.toJson(labels));
            model.addAttribute("chart_data",gson.toJson(data));
            mainController.sendUpdate(measurementList.get(0));
        }
        return "sensor_value/show";
    }
}
