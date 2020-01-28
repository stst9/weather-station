package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.SensorValue;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class SensorValueDAO extends BaseDAO {

    public SensorValueDAO(EntityManager em) {
        super(em);
    }

    public SensorValue findById(int id){
        SensorValue ret=null;
        TypedQuery<SensorValue> typedQuery=em.createQuery("select sv from SensorValue sv where sv.id=:id",getBaseClass());
        typedQuery.setParameter("id",id);
        if (typedQuery.getResultList().size()>0){
            ret=typedQuery.getSingleResult();
        }
        return ret;
    }

    @Override
    public Class getBaseClass() {
        return SensorValue.class;
    }
}
