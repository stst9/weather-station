package me.stst.weatherstation;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MainStorage {
    //private EntityManagerFactory entityManagerFactory= Persistence.createEntityManagerFactory("mysql_default");
    private SessionFactory sessionFactory=new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    private static MainStorage instance=new MainStorage();

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static MainStorage getInstance() {
        return instance;
    }
}
