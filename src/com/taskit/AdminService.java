package com.taskit;

import java.sql.*;
import java.util.Scanner;

public class AdminService {
    static Scanner sc = new Scanner(System.in);

    public  void adminMenu() {
        System.out.print("Enter Admin Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        if (email.equals("admin@taskit.com") && pass.equals("admin123")) {
            System.out.println("\n✅ Admin Logged In Successfully!");
            while (true) {
                System.out.println("\n--- Admin Menu ---");
                System.out.println("1. Add Professional");
                System.out.println("2. Update Professional Price");
                System.out.println("3. Delete Professional");
                System.out.println("4. View All Professionals");
                System.out.println("5. Logout");
                System.out.print("Enter choice: ");
                int ch = Integer.parseInt(sc.nextLine());

                switch (ch) {
                    case 1 -> addProfessional();
                    case 2 -> updateProfessional();
                    case 3 -> deleteProfessional();
                    case 4 -> viewProfessionals();
                    case 5 -> { return; }
                    default -> System.out.println("Invalid Choice!");
                }
            }
        } else {
            System.out.println("❌ Invalid Admin Credentials!");
        }
    }

     void addProfessional() {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Enter Professional Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Service Type: ");
            String service = sc.nextLine();
            System.out.print("Enter Price per Hour: ");
            double price = Double.parseDouble(sc.nextLine());
            System.out.print("Enter Area: ");
            String area = sc.nextLine();

            String query = "INSERT INTO professionals (professionals_Name, professionals_service_Type, professionals_Price_Per_Hour, professionals_Area) VALUES (?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, service);
            ps.setDouble(3, price);
            ps.setString(4, area);
            ps.executeUpdate();

            System.out.println("✅ Professional added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

     void updateProfessional() {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Enter Professional ID to update price: ");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("Enter new price per hour: ");
            double price = Double.parseDouble(sc.nextLine());

            PreparedStatement ps = con.prepareStatement("UPDATE professionals SET professionals_Price_Per_Hour=? WHERE professionals_ID=?");
            ps.setDouble(1, price);
            ps.setInt(2, id);
            int updated = ps.executeUpdate();

            System.out.println(updated > 0 ? "✅ Updated successfully!" : "❌ Professional not found!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

     void deleteProfessional() {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Enter Professional ID to delete: ");
            int id = Integer.parseInt(sc.nextLine());

            PreparedStatement ps = con.prepareStatement("DELETE FROM professionals WHERE professionals_ID=?");
            ps.setInt(1, id);
            int deleted = ps.executeUpdate();

            System.out.println(deleted > 0 ? "✅ Deleted successfully!" : "❌ Professional not found!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

     void viewProfessionals() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM professionals")) {
            System.out.println("\n--- List of Professionals ---");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | ₹%.2f/hr | %s | Available: %b%n",
                        rs.getInt("professionals_ID"), rs.getString("professionals_Name"),
                        rs.getString("professionals_service_Type"), rs.getDouble("professionals_Price_Per_Hour"),
                        rs.getString("professionals_Area"), rs.getBoolean("professionals_Available"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
