package com.kce.service;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kce.DAO.BidDAO;
import com.kce.DAO.ProjectDAO;
import com.kce.DAO.UserDAO;
import com.kce.bean.Bid;
import com.kce.bean.Project;
import com.kce.bean.User;
import com.kce.util.ActiveEngagementsExistException;
import com.kce.util.HibernateUtil;
import com.kce.util.ProjectAwardingException;
import com.kce.util.ValidationException;



public class FreelanceService {

    private UserDAO userDAO = new UserDAO();
    private ProjectDAO projectDAO = new ProjectDAO();
    private BidDAO bidDAO = new BidDAO();

    // ================= USER =================

    public User viewUserDetails(String userID) {

        if (userID == null || userID.trim().isEmpty())
            return null;

        return userDAO.findUser(userID);
    }

    public List<User> viewAllUsers() {
        return userDAO.viewAllUsers();
    }

    public boolean registerNewUser(User user) throws ValidationException {

        if (user == null ||
            user.getUserID() == null || user.getUserID().trim().isEmpty() ||
            user.getFullName() == null || user.getFullName().trim().isEmpty() ||
            user.getEmail() == null || user.getEmail().trim().isEmpty()) {

            throw new ValidationException("Required fields must not be empty");
        }

        if (!"CLIENT".equalsIgnoreCase(user.getUserRole()) &&
            !"FREELANCER".equalsIgnoreCase(user.getUserRole())) {

            throw new ValidationException("Role must be CLIENT or FREELANCER");
        }

        if (userDAO.findUser(user.getUserID()) != null)
            return false;

        user.setStatus("ACTIVE");

        return userDAO.insertUser(user);
    }

    // ================= PROJECT =================

    public boolean postNewProject(Project project) throws ValidationException {

        if (project == null ||
            project.getClientUserID() == null ||
            project.getProjectTitle() == null ||
            project.getBudgetMin() == null ||
            project.getBudgetMax() == null ||
            project.getBudgetMin().compareTo(BigDecimal.ZERO) <= 0 ||
            project.getBudgetMax().compareTo(project.getBudgetMin()) < 0) {

            throw new ValidationException("Invalid project data");
        }

        User client = userDAO.findUser(project.getClientUserID());

        if (client == null ||
            !"CLIENT".equalsIgnoreCase(client.getUserRole()) ||
            !"ACTIVE".equalsIgnoreCase(client.getStatus()))
            return false;

        project.setStatus("OPEN");
        project.setPostedDate(new java.sql.Date(System.currentTimeMillis()));

        return projectDAO.insertProject(project);
    }

    // ================= BID =================

    public boolean placeBid(int projectID,
                            String freelancerUserID,
                            BigDecimal bidAmount,
                            int deliveryDays,
                            String coverLetter)
            throws ValidationException {

        if (projectID <= 0 ||
            freelancerUserID == null ||
            bidAmount == null ||
            bidAmount.compareTo(BigDecimal.ZERO) <= 0 ||
            deliveryDays <= 0) {

            throw new ValidationException("Invalid bid data");
        }

        Project project = projectDAO.findProject(projectID);

        if (project == null ||
            !"OPEN".equalsIgnoreCase(project.getStatus()))
            return false;

        User freelancer = userDAO.findUser(freelancerUserID);

        if (freelancer == null ||
            !"FREELANCER".equalsIgnoreCase(freelancer.getUserRole()) ||
            !"ACTIVE".equalsIgnoreCase(freelancer.getStatus()))
            return false;

        if (bidDAO.findActiveBidForProjectAndFreelancer(projectID, freelancerUserID) != null)
            throw new ValidationException("Duplicate active bid not allowed");

        Bid bid = new Bid();
        bid.setProjectID(projectID);
        bid.setFreelancerUserID(freelancerUserID);
        bid.setBidAmount(bidAmount);
        bid.setDeliveryDays(deliveryDays);
        bid.setCoverLetter(coverLetter);
        bid.setBidStatus("PENDING");
        bid.setBidDate(new java.sql.Date(System.currentTimeMillis()));

        return bidDAO.insertBid(bid);
    }

    // ================= AWARD PROJECT =================

    public boolean awardProject(int projectID, int bidID)
            throws ProjectAwardingException {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Project project = session.get(Project.class, projectID);
            Bid bid = session.get(Bid.class, bidID);

            if (project == null ||
                !"OPEN".equalsIgnoreCase(project.getStatus()))
                throw new ProjectAwardingException("Project not OPEN");

            if (bid == null ||
                !"PENDING".equalsIgnoreCase(bid.getBidStatus()) ||
                bid.getProjectID() != projectID)
                throw new ProjectAwardingException("Invalid bid");

            // Accept selected bid
            bid.setBidStatus("ACCEPTED");
            session.merge(bid);

            // Reject other bids
            session.createQuery(
                "UPDATE Bid SET bidStatus='REJECTED' " +
                "WHERE projectID=:pid AND bidID<>:bidId")
                .setParameter("pid", projectID)
                .setParameter("bidId", bidID)
                .executeUpdate();

            // Update project
            project.setAwardedBidID(bidID);
            project.setStatus("AWARDED");
            session.merge(project);

            tx.commit();
            return true;

        } catch (Exception e) {

            if (tx != null)
                tx.rollback();

            throw new ProjectAwardingException("Awarding failed: " + e.getMessage());
        }
    }

    // ================= COMPLETE PROJECT =================

    public boolean markProjectCompleted(int projectID) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Project project = session.get(Project.class, projectID);

            if (project == null ||
                !"AWARDED".equalsIgnoreCase(project.getStatus()))
                return false;

            project.setStatus("COMPLETED");
            session.merge(project);

            tx.commit();
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }

    // ================= LIST =================

    public List<Bid> listBidsByProject(int projectID) {
        return bidDAO.findBidsByProject(projectID);
    }

    public List<Bid> listBidsByFreelancer(String freelancerUserID) {
        return bidDAO.findBidsByFreelancer(freelancerUserID);
    }

    // ================= REMOVE USER =================

    public boolean removeUser(String userID)
            throws ValidationException, ActiveEngagementsExistException {

        if (userID == null || userID.trim().isEmpty())
            throw new ValidationException("UserID cannot be empty");

        User user = userDAO.findUser(userID);

        if (user == null)
            return false;

        if ("CLIENT".equalsIgnoreCase(user.getUserRole())) {

            List<Project> activeProjects =
                projectDAO.findActiveProjectsByClient(userID);

            if (activeProjects != null && !activeProjects.isEmpty())
                throw new ActiveEngagementsExistException(
                        "Client has active projects");
        }

        if ("FREELANCER".equalsIgnoreCase(user.getUserRole())) {

            List<Bid> activeBids =
                bidDAO.findActiveBidsForFreelancer(userID);

            if (activeBids != null && !activeBids.isEmpty())
                throw new ActiveEngagementsExistException(
                        "Freelancer has active bids");
        }

        return userDAO.deleteUser(userID);
    }
}
