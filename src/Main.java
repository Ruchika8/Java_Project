import com.taskit.AdminService;
import com.taskit.CustomerService;
import com.taskit.DatabaseConnection;
import com.taskit.ProfessionalService;

import java.sql.Connection;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection con = DatabaseConnection.getConnection();
        if (con == null) {
            System.out.println("Database not connected!");
            return;
        }
        System.out.println("✅ Connected to TaskIt DB");
        while (true) {
            System.out.println("\n===== TASKIT MENU =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("3. Professional Login");
            System.out.println("4. Customer Registration");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int ch = Integer.parseInt(sc.nextLine());
            AdminService adminService = new AdminService();
            CustomerService customerService = new CustomerService();
            ProfessionalService professionalService = new ProfessionalService();

            switch (ch) {
                case 1 -> adminService.adminMenu();
                case 2 -> customerService.customerLogin();
                case 3 -> professionalService.professionalLogin();
                case 4 -> customerService.registerCustomer();
                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid!");
            }
        }
    }
}