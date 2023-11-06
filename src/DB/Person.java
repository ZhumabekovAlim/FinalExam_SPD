package DB;

abstract class Person {
    private String username;

    private String password;
    private String phone_number;

    public Person(String username, String password, String phone_number){
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
    }
    public Person(String username, String password){
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
}
