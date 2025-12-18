package com.atelier.dao;

import com.atelier.entities.Piece;
import com.atelier.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PieceDAO {
    
    public void save(Piece piece) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(piece);
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
    
    public void update(Piece piece) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(piece);
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
    
    public Piece findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Piece.class, id);
        } finally {
            session.close();
        }
    }
    
    public List<Piece> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Piece> query = session.createQuery("from Piece", Piece.class);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    public Piece findByRef(String ref) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Piece> query = session.createQuery(
                "from Piece p where p.ref = :ref", Piece.class);
            query.setParameter("ref", ref);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
}

