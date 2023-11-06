package WiDb;

public class AdapterBluetoothToWifi extends WifiConnection implements Bluetooth{


    @Override
    public void bluetoothConnect() {
        wifiConnect();
    }
}
