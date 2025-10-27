package com.taskit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public  class ProfessionalService {
    static Scanner sc = new Scanner(System.in);

      public void professionalLogin() {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Enter Professional ID: ");
            int id = Integer.parseInt(sc.nextLine());
            String q = "SELECT * FROM professionals WHERE professionals_ID=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("\n✅ Welcome, " + rs.getString("professionals_Name"));
                professionalMenu(id);
            } else System.out.println("❌ Professional not found!");
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

     void professionalMenu(int id) {
        while (true) {
            System.out.println("\n--- Professional Menu ---");
            System.out.println("1. View Assigned Services");
            System.out.println("2. Update Availability");
            System.out.println("3. Mark Service Completed");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int ch = Integer.parseInt(sc.nextLine());
            switch (ch) {
                case 1 -> viewAssigned(id);
                case 2 -> updateAvailability(id);
                case 3 -> markCompleted(id);
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid!");
            }
        }
    }

     void viewAssigned(int id) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String q = "SELECT sr.srvic_req_ID, u.users_Name AS customer, sr.status, sr.date FROM service_requests sr JOIN users u ON sr.Customer_ID=u.users_ID WHERE sr.Professionals_id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            System.out.println(" S.Req ID |  Cust Name  | Service Status | Service Date ");
            System.out.println("------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("   %d   | %s | %s | %s%n",
                        rs.getInt("srvic_req_ID"), rs.getString("customer"), rs.getString("status"), rs.getString("date"));
            }
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

     void updateAvailability(int id) {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Available (true/false): ");
            boolean a = Boolean.parseBoolean(sc.nextLine());
            PreparedStatement ps = con.prepareStatement("UPDATE professionals SET professionals_Available=? WHERE professionals_ID=?");
            ps.setBoolean(1, a);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("✅ Availability updated!");
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

     void markCompleted(int id) {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Enter Request ID: ");
            int rid = Integer.parseInt(sc.nextLine());
            PreparedStatement ps = con.prepareStatement("UPDATE service_requests SET status='Completed' WHERE srvic_req_ID=? AND Professionals_id=?");
            ps.setInt(1, rid);
            ps.setInt(2, id);
            int r = ps.executeUpdate();
            if (r > 0) {
                PreparedStatement psUpdate = con.prepareStatement("UPDATE professionals SET professionals_Available=true WHERE professionals_ID=?");
                psUpdate.setInt(1, id);
                psUpdate.executeUpdate();
                System.out.println("✅ Service marked completed!");
            } else System.out.println("❌ Invalid request!");
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}
