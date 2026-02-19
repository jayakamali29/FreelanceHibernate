package com.kce.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.kce.bean.Project;
import com.kce.util.HibernateUtil;

public class ProjectDAO {

    // ================= FIND BY ID =================
    public Project findProject(int projectID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Project.class, projectID);
        }
    }

    // ================= VIEW ALL =================
    public List<Project> viewAllProjects() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Project", Project.class)
                          .getResultList();
        }
    }

    // ================= VIEW OPEN PROJECTS =================
    public List<Project> viewOpenProjects() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Project> query = session.createQuery(
                    "FROM Project WHERE status = :status",
                    Project.class);

            query.setParameter("status", "OPEN");

            return query.getResultList();
        }
    }

    // ================= INSERT =================
    public boolean insertProject(Project project) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.persist(project);

            tx.commit();
            return true;

        } catch (Exception e) {

            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return false;
    }

    // ================= UPDATE STATUS =================
    public boolean updateProjectStatus(int projectID, String status) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Project project = session.get(Project.class, projectID);

            if (project == null)
                return false;

            project.setStatus(status);

            session.merge(project);

            tx.commit();
            return true;

        } catch (Exception e) {

            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return false;
    }

    // ================= UPDATE AWARDED BID =================
    public boolean updateAwardedBid(int projectID,
                                    int bidID,
                                    String status) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Project project = session.get(Project.class, projectID);

            if (project == null)
                return false;

            project.setAwardedBidID(bidID);
            project.setStatus(status);

            session.merge(project);

            tx.commit();
            return true;

        } catch (Exception e) {

            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return false;
    }

    // ================= ACTIVE PROJECTS BY CLIENT =================
    public List<Project> findActiveProjectsByClient(String clientUserID) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Project> query = session.createQuery(
                    "FROM Project WHERE clientUserID = :cid " +
                    "AND status IN ('OPEN','AWARDED')",
                    Project.class);

            query.setParameter("cid", clientUserID);

            return query.getResultList();
        }
    }

    // ================= DELETE PROJECT =================
    public boolean deleteProject(int projectID) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Project project = session.get(Project.class, projectID);

            if (project != null) {
                session.remove(project);
                tx.commit();
                return true;
            }

        } catch (Exception e) {

            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return false;
    }
}
