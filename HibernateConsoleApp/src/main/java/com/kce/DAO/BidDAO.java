package com.kce.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.kce.bean.Bid;
import com.kce.util.HibernateUtil;

public class BidDAO {

    // ================= INSERT =================
    public boolean insertBid(Bid bid) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(bid);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    // ================= FIND BY ID =================
    public Bid findBid(int bidID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Bid.class, bidID);
        }
    }

    // ================= FIND BY PROJECT =================
    public List<Bid> findBidsByProject(int projectID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Bid> query = session.createQuery(
                    "FROM Bid WHERE projectID = :pid", Bid.class);

            query.setParameter("pid", projectID);

            return query.getResultList();
        }
    }

    // ================= FIND BY FREELANCER =================
    public List<Bid> findBidsByFreelancer(String freelancerUserID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Bid> query = session.createQuery(
                    "FROM Bid WHERE freelancerUserID = :fid", Bid.class);

            query.setParameter("fid", freelancerUserID);

            return query.getResultList();
        }
    }

    // ================= FIND ACTIVE BID =================
    public Bid findActiveBidForProjectAndFreelancer(int projectID,
                                                    String freelancerUserID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Bid> query = session.createQuery(
                    "FROM Bid WHERE projectID = :pid " +
                    "AND freelancerUserID = :fid " +
                    "AND bidStatus = :status", Bid.class);

            query.setParameter("pid", projectID);
            query.setParameter("fid", freelancerUserID);
            query.setParameter("status", "PENDING");

            return query.uniqueResultOptional().orElse(null);
        }
    }

    // ================= UPDATE STATUS =================
    public boolean updateBidStatus(int bidID, String status) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Bid bid = session.get(Bid.class, bidID);

            if (bid == null)
                return false;

            bid.setBidStatus(status);

            session.merge(bid);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    // ================= BULK REJECT =================
    public boolean bulkRejectOtherBids(int projectID, int acceptedBidID) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Query<?> query = session.createQuery(
                    "UPDATE Bid SET bidStatus = :status " +
                    "WHERE projectID = :pid AND bidID <> :bidId");

            query.setParameter("status", "REJECTED");
            query.setParameter("pid", projectID);
            query.setParameter("bidId", acceptedBidID);

            query.executeUpdate();

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return false;
    }

    // ================= ACTIVE BIDS FOR FREELANCER =================
    public List<Bid> findActiveBidsForFreelancer(String freelancerUserID) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Bid> query = session.createQuery(
                    "FROM Bid WHERE freelancerUserID = :fid " +
                    "AND bidStatus NOT IN ('REJECTED','WITHDRAWN')",
                    Bid.class);

            query.setParameter("fid", freelancerUserID);

            return query.getResultList();
        }
    }

    // ================= DELETE =================
    public boolean deleteBid(int bidID) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Bid bid = session.get(Bid.class, bidID);

            if (bid != null) {
                session.remove(bid);
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
