package com.atelier.dao;

import com.atelier.entities.OrdreReparation;
import com.atelier.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class OrdreReparationDAO {
    
    public void save(OrdreReparation ordreReparation) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(ordreReparation);
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
    
    public void update(OrdreReparation ordreReparation) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(ordreReparation);
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
    
    public OrdreReparation findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<OrdreReparation> query = session.createQuery(
                "select distinct o from OrdreReparation o left join fetch o.lignesPrestation left join fetch o.facture where o.id = :id", OrdreReparation.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
    
    public List<OrdreReparation> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<OrdreReparation> query = session.createQuery(
                "select distinct o from OrdreReparation o left join fetch o.lignesPrestation left join fetch o.facture", OrdreReparation.class);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    public List<OrdreReparation> findByEtat(OrdreReparation.EtatReparation etat) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<OrdreReparation> query = session.createQuery(
                "select distinct o from OrdreReparation o left join fetch o.lignesPrestation left join fetch o.facture where o.etat = :etat", OrdreReparation.class);
            query.setParameter("etat", etat);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    public List<OrdreReparation> findByVehiculeId(Long vehiculeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<OrdreReparation> query = session.createQuery(
                "select distinct o from OrdreReparation o left join fetch o.lignesPrestation left join fetch o.facture where o.vehicule.id = :vehiculeId", OrdreReparation.class);
            query.setParameter("vehiculeId", vehiculeId);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    public List<OrdreReparation> findEnCours() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<OrdreReparation> query = session.createQuery(
                "select distinct o from OrdreReparation o left join fetch o.lignesPrestation left join fetch o.facture where o.etat != :termine", OrdreReparation.class);
            query.setParameter("termine", OrdreReparation.EtatReparation.TERMINE);
            return query.list();
        } finally {
            session.close();
        }
    }
}

