package com.kce.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.kce.bean.User;
import com.kce.util.HibernateUtil;

public class UserDAO {

    // ================= FIND BY ID =================
    public User findUser(String userID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, userID);
        }
    }

    // ================= VIEW ALL USERS =================
    public List<User> viewAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class)
                          .getResultList();
        }
    }

    // ================= INSERT USER =================
    public boolean insertUser(User user) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.persist(user);

            tx.commit();
            return true;

        } catch (Exception e) {

            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return false;
    }

    // ================= UPDATE STATUS =================
    public boolean updateUserStatus(String userID, String status) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            User user = session.get(User.class, userID);

            if (user == null)
                return false;

            user.setStatus(status);

            session.merge(user);

            tx.commit();
            return true;

        } catch (Exception e) {

            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return false;
    }

    // ================= DELETE USER =================
    public boolean deleteUser(String userID) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            User user = session.get(User.class, userID);

            if (user != null) {

                session.remove(user);

                tx.commit();
                return true;
            }

        } catch (Exception e) {

            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return false;
    }

    // ================= FIND BY ROLE (Extra Useful Method) =================
    public List<User> findUsersByRole(String role) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<User> query = session.createQuery(
                    "FROM User WHERE userRole = :role",
                    User.class);

            query.setParameter("role", role);

            return query.getResultList();
        }
    }

    // ================= FIND ACTIVE USERS =================
    public List<User> findActiveUsers() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<User> query = session.createQuery(
                    "FROM User WHERE status = :status",
                    User.class);

            query.setParameter("status", "ACTIVE");

            return query.getResultList();
        }
    }
}
