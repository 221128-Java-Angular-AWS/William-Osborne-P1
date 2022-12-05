

import java.util.Scanner;
public class UserLogin {
    public static String login(){
        String employeeLogin = "user";
        String adminLogin = "admin";
        String password = "pass";



        // maybe make this try with resources
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter username: ");
        String userInput = sc.nextLine();
        System.out.println("Please enter password for " + userInput + " : ");
        String passInput = sc.nextLine();
        sc.close();

        if (userInput.equals(employeeLogin) && passInput.equals(password)){
            User user1 = new User(employeeLogin, "Employee");
            return "Login successful.";
        } else if (userInput.equals(adminLogin) && passInput.equals(password)){
            User user1 = new User(employeeLogin, "Manager");
            return "Login successful.";
        } else {
            return "Invalid credentials.";
        }
    }



}
