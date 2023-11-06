package DB;
import Devices.Device;
import Devices.DevicesFactory;
import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {
    public static String username;
    public static int kVT;
    public static User user;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you have an account? (1-no, 2-yes)");
        int acc = scanner.nextInt();
        if (acc == 1) {
            createAccount(scanner);
        }else if(acc == 2){
            loginAccount(scanner);
        }else{
            System.out.println("Input 1 or 2 numbers!");
        }

        while (true) {

            if(kVT>=30){
               user.update(kVT);
            }
            System.out.println("-----------------------------------");
            System.out.println("|Chose need action:               |");
            System.out.println("|1. View your devices             |");
            System.out.println("|2. Control your devices          |");
            System.out.println("|3. Add new device                |");
            System.out.println("|4. Delete device                 |");
            System.out.println("|5. Expenses                      |");
            System.out.println("|6. Exit from application         |");
            System.out.println("-----------------------------------");

            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    allDevices(scanner,username);
                break;
                case 2:
                    factory(scanner);
                    break;
                case 3:
                    addNewDevices(scanner);
                    break;
                case 4:
                    deleteDevice(scanner);
                    break;
                case 5:
                    System.out.println(kVT);
                    break;
                case 6:
                    exit(0);
                default:
                    System.out.println("Chose correct number!");
            }
        }
        }

    private static void createAccount(Scanner scanner) throws SQLException, ClassNotFoundException {
        DatabaseManager d = new DatabaseManager();
        System.out.println("Input your username: ");
        String username = scanner.next();
        System.out.println("Input your password: ");
        String password = scanner.next();
        System.out.println("Input your phone number: ");
        String phone_number = scanner.next();
        User account = new User(username,password,phone_number);
        d.saveUser(account);
        d.enterUser(username,password);
        user = new User(username, password);
    }

    private static void loginAccount(Scanner scanner) throws SQLException, ClassNotFoundException {
        DatabaseManager d = new DatabaseManager();
        System.out.println("Input your account info: ");
        boolean auth = true;
        while (auth) {
            System.out.println("Username:");
            String username1 = scanner.next();
            System.out.println("Password:");
            String password = scanner.next();
            auth = d.enterUser(username1,password);
            if(auth == true){

                System.out.println("Incorrect account data!");
            }else {
                user = new User(username1, password);
                System.out.println("Correct");
                username = username1;
                break;
            }
        }
    }

    private static void allDevices(Scanner scanner,String username) throws SQLException, ClassNotFoundException {
        DatabaseManager d = new DatabaseManager();
        System.out.println("Your devices: ");
        d.viewDevices(username);
    }
    private static void addNewDevices(Scanner scanner) throws SQLException, ClassNotFoundException {
        DatabaseManager d = new DatabaseManager();
        System.out.println("Your devices: ");
        d.addDevice(username);
    }

    private static void factory(Scanner scanner) throws SQLException, ClassNotFoundException {
        DatabaseManager d = new DatabaseManager();
        d.viewDevices(username);
        System.out.println("Chose device for control:\n");
        int type = scanner.nextInt();
        Device device =  DevicesFactory.action(type);
        d.changeState(username,type,device,kVT);
        boolean nowState = d.getState(username,type,device,kVT);
        if(nowState==true){
            kVT += device.kVT();
        }
        d.connection(username);
    }
    private static void deleteDevice(Scanner scanner) throws SQLException, ClassNotFoundException {
        DatabaseManager d = new DatabaseManager();
        System.out.println("Your devices: ");
        d.removeDevice(username);
    }
}
