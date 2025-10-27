package com.taskit;

import java.sql.*;
import java.util.Scanner;

public  class CustomerService {
    static Scanner sc = new Scanner(System.in);

    public  void registerCustomer() {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            System.out.print("Enter Password: ");
            String pass = sc.nextLine();

            String query = "INSERT INTO users (users_Name, users_Email, users_Password, users_Role) VALUES (?, ?, ?, 'customer')";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, pass);
            ps.executeUpdate();

            System.out.println("✅ Registration successful!");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate"))
                System.out.println("❌ This email is already registered.");
            else
                System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public  void customerLogin() {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            System.out.print("Enter Password: ");
            String pass = sc.nextLine();

            String query = "SELECT * FROM users WHERE users_Email=? AND users_Password=? AND users_Role='customer'";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n✅ Welcome, " + rs.getString("users_Name"));
                int cid = rs.getInt("users_ID");
                customerMenu(cid);
            } else System.out.println("❌ Invalid login!");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

     void customerMenu(int cid) {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View All Professionals");
            System.out.println("2. Search by Service Type");
            System.out.println("3. Hire Professional");
            System.out.println("4. View My Hired Services");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> viewProfessionals();
                case 2 -> searchByServiceType();
                case 3 -> hireProfessional(cid);
                case 4 -> viewMyServices(cid);
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid!");
            }
        }
    }


     void viewProfessionals() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM professionals WHERE professionals_Available=true")) {
            System.out.println("\n--- Professionals ---");
            System.out.println("Prof ID | Prof Name  | Service Type | Price/hr |  Area ");
            System.out.println("------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%d | %s   | %s  |  ₹%.2f/hr | %s%n",
                        rs.getInt("professionals_ID"), rs.getString("professionals_Name"),
                        rs.getString("professionals_service_Type"), rs.getDouble("professionals_Price_Per_Hour"),
                        rs.getString("professionals_Area"));
            }
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }


     void searchByServiceType() {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Enter Service Type: ");
            String type = sc.nextLine();
            String query = "SELECT * FROM professionals WHERE professionals_service_Type LIKE ? AND professionals_Available=true";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + type + "%");
            ResultSet rs = ps.executeQuery();
            System.out.println(" Prof ID |  Prof Name  |   Service Type   |  Price/hr |  Area ");
            System.out.println("------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | ₹%.2f/hr | %s%n",
                        rs.getInt("professionals_ID"), rs.getString("professionals_Name"),
                        rs.getString("professionals_service_Type"), rs.getDouble("professionals_Price_Per_Hour"),
                        rs.getString("professionals_Area"));
            }
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

     void hireProfessional(int cid) {
        try (Connection con = DatabaseConnection.getConnection()) {
            System.out.print("Enter Professional ID: ");
            int pid = Integer.parseInt(sc.nextLine());
            String q = "INSERT INTO service_requests (Customer_ID, Professionals_id, status) VALUES (?, ?, 'Pending')";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, cid);
            ps.setInt(2, pid);
            ps.executeUpdate();
            con.prepareStatement("UPDATE professionals SET professionals_Available=false WHERE professionals_ID=" + pid).executeUpdate();
            System.out.println("✅ Service booked!");
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

     void viewMyServices(int cid) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String q = "SELECT sr.srvic_req_ID, p.professionals_Name, p.professionals_service_Type, sr.status, sr.date FROM service_requests sr " +
                    "JOIN professionals p ON sr.Professionals_id=p.professionals_ID WHERE sr.Customer_ID=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            System.out.println("Service ID |  Pro Name  | Service Type  | Service Status |  Service Date ");
            System.out.println("------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("  %d   | %s | %s | %s | %s%n",
                        rs.getInt("srvic_req_ID"), rs.getString("professionals_Name"),
                        rs.getString("professionals_service_Type"), rs.getString("status"),
                        rs.getString("date"));
            }
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}