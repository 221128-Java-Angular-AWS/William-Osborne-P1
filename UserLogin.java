import java.util.Scanner;
public class UserLogin {
    public static String login(){
        String username = "user";
        String password = "pass";

        // maybe make this try with resources
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter username: ");
        String userInput = sc.nextLine();
        System.out.println("Please enter password for " + userInput + " : ");
        String passInput = sc.nextLine();
        sc.close();

        if (userInput.equals(username) && passInput.equals(password)){
            return "Login successful.";
        } else{
            return "Invalid credentials.";
        }
    }



}
