package me.stst.weatherstation.repository;

import me.stst.weatherstation.domain.Unit;

import javax.persistence.*;

public class UnitDAO extends BaseDAO{


    public UnitDAO(EntityManager em) {
        super(em);
    }

    public Unit findByName(String name){
        em.getTransaction().begin();
        TypedQuery<Unit> query=em.createQuery("select u from Unit u where u.name=:name",Unit.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public Class getBaseClass() {
        return Unit.class;
    }
}
