package DB;

import Devices.Coffee.MilkFrotherDecorator;
import Devices.CoffeeMachine;
import Devices.Device;
import WiDb.AdapterWifiToBluetooth;
import WiDb.BluetoothConnection;
import java.sql.*;
import java.util.Scanner;

public class DatabaseManager {

    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:postgresql://localhost:5432/smart_house";

        Class.forName("org.postgresql.Driver");
        dbConnection = DriverManager.getConnection(connectionString, "postgres", "Alimoka040102");
        return dbConnection;
    }

    public void saveUser(User user) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = getDbConnection().prepareStatement("SELECT * FROM users WHERE phone_number = ?");
        stmt.setString(1, user.getPhone_number());
        ResultSet rs = stmt.executeQuery();
        if(rs != null){
            PreparedStatement stmt1 = getDbConnection().prepareStatement("INSERT INTO users (username,password,phone_number) VALUES (?,?,?)");
            stmt1.setString(1,user.getUsername());
            stmt1.setString(2, user.getPassword());
            stmt1.setString(3, user.getPhone_number());
            stmt1.executeUpdate();
        }else{
            System.out.println("This user is already registered!");
        }
    }

    public boolean enterUser(String username, String password) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = getDbConnection().prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        stmt.setString(1,username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            return false;
        }
        return true;
    }

         public void viewDevices(String username) throws SQLException, ClassNotFoundException {
             PreparedStatement stmt = getDbConnection().prepareStatement("SELECT d.name,d.id FROM user_devices ud JOIN devices d ON ud.device_id = d.id JOIN users u ON ud.user_id = u.id WHERE u.username = ? ");
             stmt.setString(1,username);
             ResultSet rs = stmt.executeQuery();
             String name = "";
             int num =1;
             int id=0;

             while (rs.next()) {
                 name = rs.getString("name");
                 id = rs.getInt("id");
                 System.out.println(id+". "+name);
             }
             System.out.println("\n");
         }

    public void addDevice(String username) throws SQLException, ClassNotFoundException {
        System.out.println("Select need device for your house: ");
        PreparedStatement stmt = getDbConnection().prepareStatement("SELECT * FROM devices WHERE id NOT IN(SELECT device_id FROM user_devices ud JOIN users u ON ud.user_id = u.id WHERE u.username = ?) ");
        stmt.setString(1,username);
        String name="";
        int id=0;
        int u_id =0;
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            name = rs.getString("name");
            id = rs.getInt("id");
            System.out.println(id+". "+name);
        }
        Scanner scanner = new Scanner(System.in);
        int d_id = scanner.nextInt();  System.out.println("\n");
        PreparedStatement stmt1 = getDbConnection().prepareStatement("SELECT * FROM users WHERE username=? ");
        stmt1.setString(1,username);
        ResultSet rs1 = stmt1.executeQuery();
        while (rs1.next()) {
        u_id = rs1.getInt("id");
        }
        PreparedStatement stmt2 = getDbConnection().prepareStatement("INSERT INTO user_devices (user_id,device_id,state) VALUES(?,?,false) ");
        stmt2.setInt(1,u_id);
        stmt2.setInt(2,d_id);
        stmt2.executeUpdate();

    }

    public void removeDevice(String username) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = getDbConnection().prepareStatement("SELECT d.name,ud.device_id FROM user_devices ud JOIN devices d ON ud.device_id = d.id JOIN users u ON ud.user_id = u.id WHERE u.username = ? ");
        stmt.setString(1,username);
        ResultSet rs = stmt.executeQuery();
        String name = "";
        int num =0;

        while (rs.next()) {
            name = rs.getString("name");
            num = rs.getInt("device_id");
            System.out.println(num+". "+name);
        }
        System.out.println("\n Select delete device from your house(id):");
        Scanner scanner = new Scanner(System.in);
        int delete_id = scanner.nextInt();
        PreparedStatement stmt2 = getDbConnection().prepareStatement("SELECT * FROM users WHERE username=? ");
        stmt2.setString(1,username);
        ResultSet rs1 = stmt2.executeQuery();
        int u_id =0;
        while (rs1.next()) {
            u_id = rs1.getInt("id");
            PreparedStatement stmt1 = getDbConnection().prepareStatement("DELETE FROM user_devices WHERE user_id =? And device_id=?");
            stmt1.setInt(1,u_id);
            stmt1.setInt(2,delete_id);
            stmt1.execute();
        }
    }

    public void changeState(String username, int type, Device device,int kVT) throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt = getDbConnection().prepareStatement("SELECT d.name,d.id,ud.state,ud.user_id FROM user_devices ud JOIN devices d ON ud.device_id = d.id JOIN users u ON ud.user_id = u.id WHERE u.username = ? AND ud.device_id =? ");
        stmt.setString(1,username);
        stmt.setInt(2,type);
        ResultSet rs = stmt.executeQuery();
        boolean state =true;
        int turn=0;

        while (rs.next()) {
            state = rs.getBoolean("state");
            int user_id = rs.getInt("user_id");
            int device_id = rs.getInt("id");
            if(state==false){
                System.out.println("Do you need turn on this device? (1 - yes, 2 - no) ");
                turn = scanner.nextInt();
                if(turn == 1){
                    device.turnon(user_id,device_id);
                    if(device_id==3) {
                        System.out.println("Do you want to add foamed milk? (1 - yes, 2 - no)");
                        int milk = scanner.nextInt();
                        if (milk==1){
                            Device coffee = new MilkFrotherDecorator(new CoffeeMachine(kVT));
                            coffee.turnon(getUserId(username), type);
                        }else if(milk == 2){
                            Device coffee = new CoffeeMachine(kVT);
                            coffee.turnon(getUserId(username), type);
                        }
                    }
                }else{
                    break;
                }
            }else{
                System.out.println("Do you need turn off this device? (1 - yes, 2 - no)");
                turn = scanner.nextInt();
                if(turn == 1){
                    device.turnoff(user_id,device_id);
                }else{
                    break;
                }
            }
        }
        System.out.println("\n");
    }
    public void connection(String username) {
        int type;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do u wanna connect bluetooth (1-yes, 2-no)");
        type = scanner.nextInt();
        if(type==1){
            System.out.println("It will be connected automatically along with Wi-Fi (1-yes, 2-no)");
            type = scanner.nextInt();
            if(type==1){
               BluetoothConnection con = new AdapterWifiToBluetooth();
               con.bluetoothConnect();
            }
        }else{
            System.out.println("Your action has been canceled");
        }
    }
    public int getUserId(String username) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt2 = getDbConnection().prepareStatement("SELECT * FROM users WHERE username=? ");
        stmt2.setString(1,username);
        ResultSet rs1 = stmt2.executeQuery();
        int u_id =0;
        while (rs1.next()) {
            u_id = rs1.getInt("id");

        }
        return u_id;
    }

    public boolean getState(String username, int type, Device device,int kVT) throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement stmt = getDbConnection().prepareStatement("SELECT ud.state FROM user_devices ud JOIN devices d ON ud.device_id = d.id JOIN users u ON ud.user_id = u.id WHERE u.username = ? AND ud.device_id =? ");
        stmt.setString(1, username);
        stmt.setInt(2, type);
        ResultSet rs = stmt.executeQuery();
        boolean state = true;
        int turn = 0;

        while (rs.next()) {
            state = rs.getBoolean("state");

        }
        return state;
    }
}
