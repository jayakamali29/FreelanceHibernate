package com.kce.app;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import com.kce.bean.Project;
import com.kce.bean.User;
import com.kce.service.FreelanceService;
import com.kce.util.ActiveEngagementsExistException;
import com.kce.util.ProjectAwardingException;
import com.kce.util.ValidationException;



public class FreelanceMain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        FreelanceService service = new FreelanceService();

        int choice;

        do {
            System.out.println("\n===============================");
            System.out.println(" Freelance Marketplace System ");
            System.out.println("===============================");
            System.out.println("1. Register User");
            System.out.println("2. Post Project");
            System.out.println("3. Place Bid");
            System.out.println("4. Award Project");
            System.out.println("5. Mark Project Completed");
            System.out.println("6. View Users");
            System.out.println("7. View Projects");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            try {

                switch (choice) {

                // ================= REGISTER USER =================
                case 1:

                    User user = new User();

                    System.out.print("Enter User ID: ");
                    user.setUserID(sc.nextLine());

                    System.out.print("Enter Full Name: ");
                    user.setFullName(sc.nextLine());

                    System.out.print("Enter Email: ");
                    user.setEmail(sc.nextLine());

                    System.out.print("Enter Mobile: ");
                    user.setMobile(sc.nextLine());

                    System.out.print("Enter Role (CLIENT/FREELANCER): ");
                    user.setUserRole(sc.nextLine());

                    System.out.print("Enter Skill/Company: ");
                    user.setPrimarySkillOrCompany(sc.nextLine());

                    if (service.registerNewUser(user))
                        System.out.println("✅ User Registered Successfully!");
                    else
                        System.out.println("❌ User already exists.");

                    break;

                // ================= POST PROJECT =================
                case 2:

                    Project project = new Project();

                    System.out.print("Enter Client User ID: ");
                    project.setClientUserID(sc.nextLine());

                    System.out.print("Enter Project Title: ");
                    project.setProjectTitle(sc.nextLine());

                    System.out.print("Enter Description: ");
                    project.setProjectDescription(sc.nextLine());

                    System.out.print("Enter Budget Min: ");
                    project.setBudgetMin(new BigDecimal(sc.nextLine()));

                    System.out.print("Enter Budget Max: ");
                    project.setBudgetMax(new BigDecimal(sc.nextLine()));

                    if (service.postNewProject(project))
                        System.out.println("✅ Project Posted Successfully!");
                    else
                        System.out.println("❌ Project Posting Failed.");

                    break;

                // ================= PLACE BID =================
                case 3:

                    System.out.print("Enter Project ID: ");
                    int pid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Freelancer User ID: ");
                    String fid = sc.nextLine();

                    System.out.print("Enter Bid Amount: ");
                    BigDecimal amount = new BigDecimal(sc.nextLine());

                    System.out.print("Enter Delivery Days: ");
                    int days = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Cover Letter: ");
                    String cover = sc.nextLine();

                    if (service.placeBid(pid, fid, amount, days, cover))
                        System.out.println("✅ Bid Placed Successfully!");
                    else
                        System.out.println("❌ Bid Failed.");

                    break;

                // ================= AWARD PROJECT =================
                case 4:

                    System.out.print("Enter Project ID: ");
                    int projectId = sc.nextInt();

                    System.out.print("Enter Bid ID to Accept: ");
                    int bidId = sc.nextInt();

                    if (service.awardProject(projectId, bidId))
                        System.out.println("✅ Project Awarded Successfully!");
                    else
                        System.out.println("❌ Award Failed.");

                    break;

                // ================= COMPLETE PROJECT =================
                case 5:

                    System.out.print("Enter Project ID: ");
                    int compId = sc.nextInt();

                    if (service.markProjectCompleted(compId))
                        System.out.println("✅ Project Marked as Completed!");
                    else
                        System.out.println("❌ Operation Failed.");

                    break;

                // ================= VIEW USERS =================
                case 6:

                    List<User> users = service.viewAllUsers();

                    if (users.isEmpty()) {
                        System.out.println("No Users Found.");
                    } else {
                        for (User u : users) {
                            System.out.println("--------------------------------");
                            System.out.println("ID: " + u.getUserID());
                            System.out.println("Name: " + u.getFullName());
                            System.out.println("Role: " + u.getUserRole());
                            System.out.println("Status: " + u.getStatus());
                        }
                    }

                    break;

                // ================= VIEW PROJECTS =================
                case 7:

                    service.viewAllUsers(); // you can modify to projectDAO.viewAllProjects()

                    System.out.println("⚠ Implement project listing display here.");

                    break;

                case 8:
                    System.out.println("Thank You for Using System!");
                    break;

                default:
                    System.out.println("Invalid Choice!");

                }

            } catch (ValidationException e) {
                System.out.println("⚠ Validation Error: " + e.getMessage());

            } catch (ProjectAwardingException e) {
                System.out.println("⚠ Award Error: " + e.getMessage());

            } catch (ActiveEngagementsExistException e) {
                System.out.println("⚠ Cannot Remove User: " + e.getMessage());

            } catch (Exception e) {
                System.out.println("⚠ Unexpected Error: " + e.getMessage());
            }

        } while (choice != 8);

        sc.close();
    }
}
