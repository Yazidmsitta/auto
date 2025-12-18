package com.atelier.dao;

import com.atelier.entities.Facture;
import com.atelier.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class FactureDAO {
    
    public void save(Facture facture) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(facture);
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
    
    public Facture findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Facture.class, id);
        } finally {
            session.close();
        }
    }
    
    public List<Facture> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Facture> query = session.createQuery("from Facture", Facture.class);
            return query.list();
        } finally {
            session.close();
        }
    }
}

