package DB;

import DB.Observer.Observer;

public class User extends Person implements Observer {
    private String username;

    private String password;
    private String phone_number;

    public User(String username, String password, String phone_number){
      super(username,password,phone_number);
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
    }
    public User(String username, String password){
        super(username,password);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public void update(int kVT){
        System.out.println("ALERT! \nToday you have spent more than 30 kVT \n");
    }
}
