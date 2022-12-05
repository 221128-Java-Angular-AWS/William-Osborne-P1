

public class User {
    private String username;
    private String userRole;

    public User(String username, String userRole){
        this.username = username;
        this.userRole = userRole;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setUserRole(String userRole){
        this.userRole = userRole;
    }

    public String getUserRole(){
        return userRole;
    }
}
