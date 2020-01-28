package me.stst.weatherstation.repository;

import me.stst.weatherstation.MainStorage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class BaseDAO {
    protected EntityManager em;

    public BaseDAO(EntityManager em) {
        this.em = em;
    }

    public abstract Class getBaseClass();
}
