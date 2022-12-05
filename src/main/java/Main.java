

public class Main {
    public static void main(String[] args){
        System.out.println("Starting here");
        String loginStatus = UserLogin.login();
        System.out.println(loginStatus);
        if (loginStatus == "Login Successful"){
            User user = new User("user", "admin");
        }
    }

}
