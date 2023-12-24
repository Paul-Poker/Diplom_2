package data;

public class UserCredentials {

    private String email;
    private String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentials() {
    }

    public static UserCredentials from (UserData user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

