package com.atelier.dao;

import com.atelier.entities.Vehicule;
import com.atelier.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class VehiculeDAO {
    
    public void save(Vehicule vehicule) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(vehicule);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void update(Vehicule vehicule) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(vehicule);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public Vehicule findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Vehicule.class, id);
        } finally {
            session.close();
        }
    }
    
    public List<Vehicule> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Vehicule> query = session.createQuery("from Vehicule", Vehicule.class);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    public List<Vehicule> findByClientId(Long clientId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Vehicule> query = session.createQuery(
                "from Vehicule v where v.client.id = :clientId", Vehicule.class);
            query.setParameter("clientId", clientId);
            return query.list();
        } finally {
            session.close();
        }
    }
}

