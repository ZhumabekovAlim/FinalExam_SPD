package Devices.Coffee;

import Devices.Device;

import java.sql.PreparedStatement;
import java.sql.SQLException;



public class CoffeeMachineDecorator implements Device {
    Device coffeeMachine;
    public CoffeeMachineDecorator(Device coffeeMachine){
        this.coffeeMachine = coffeeMachine;
    }

    @Override
    public void turnon(int user_id,int device_id) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = d.getDbConnection().prepareStatement("UPDATE user_devices SET state=? WHERE user_id=? AND device_id=?");
        stmt.setBoolean(1, true);
        stmt.setInt(2, user_id);
        stmt.setInt(3, device_id);
        stmt.executeUpdate();
        System.out.println("Coffee machine start brew coffee");
    }

    @Override
    public void turnoff(int user_id,int device_id) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = d.getDbConnection().prepareStatement("UPDATE user_devices SET state=? WHERE user_id=? AND device_id=?");
        stmt.setBoolean(1, false);
        stmt.setInt(2, user_id);
        stmt.setInt(3, device_id);
        stmt.executeUpdate();
    }

    @Override
    public int kVT() {
        return 0;
    }
}
