package Devices;

import DB.DatabaseManager;

import java.sql.SQLException;

public interface Device {
    void turnon(int user_id,int device_id) throws SQLException, ClassNotFoundException;
    void turnoff(int user_id,int device_id) throws SQLException, ClassNotFoundException;
    DatabaseManager d = new DatabaseManager();
    int kVT();
}

