package me.stst.weatherstation.rest;

import me.stst.weatherstation.MainStorage;
import me.stst.weatherstation.domain.Device;
import me.stst.weatherstation.domain.Sensor;
import me.stst.weatherstation.domain.SensorValue;
import me.stst.weatherstation.domain.Unit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@RestController("/rest/control")
public class MainController {
    EntityManagerFactory entityManagerFactory= MainStorage.getInstance().getSessionFactory();

    @GetMapping("seed_units")
    public Boolean seedUnits(){
        boolean ret=false;
        EntityManager entityManager=entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Unit[] units=new Unit[]{
                new Unit("Degrees celsius","","°C"),
                new Unit("Relative humidity","","% RH"),
                new Unit("Air pressure (hPa)","","hPa"),
                new Unit("UV Index","UVI",""),
                new Unit("Power per area","","µW/cm2"),
                new Unit("Parts per billion","","ppb"),
                new Unit("Parts per million","","ppm")
        };
        for (Unit unit:units){
            entityManager.persist(unit);
        }
        entityManager.getTransaction().commit();
        return ret;
    }

    @GetMapping("seed_test_dev")
    public Boolean seedArduino(){
        EntityManager entityManager=entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Device device=new Device("Arduino1");
        Sensor[] sensors=new Sensor[]{
                new Sensor("Temperature","DS18B20",device),
                new Sensor("Bosch temperature, humidity and pressure","BME280",device),
                new Sensor("UV","VEML6075",device),
                new Sensor("TVOC and eCO2","SGP30",device)
        };
        SensorValue[] sensorValues=new SensorValue[]{
                new SensorValue("Temperature",entityManager.find(Unit.class,1),sensors[0]),
                new SensorValue("Temperature",entityManager.find(Unit.class,1),sensors[1]),
                new SensorValue("Humidity",entityManager.find(Unit.class,2),sensors[1]),
                new SensorValue("Pressure",entityManager.find(Unit.class,3),sensors[1]),
                new SensorValue("UV Index",entityManager.find(Unit.class,4),sensors[2]),
                new SensorValue("UVA",entityManager.find(Unit.class,5),sensors[2]),
                new SensorValue("UVB",entityManager.find(Unit.class,5),sensors[2]),
                new SensorValue("TVOC",entityManager.find(Unit.class,6),sensors[3]),
                new SensorValue("eCO2",entityManager.find(Unit.class,7),sensors[3]),
        };
        for (SensorValue value:sensorValues){
            entityManager.persist(value);
        }
        entityManager.getTransaction().commit();
        return true;
    }
}
