package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;

public interface UnitDAO extends JpaRepository<Unit,Integer> {

    Unit findByName(String name);
}
