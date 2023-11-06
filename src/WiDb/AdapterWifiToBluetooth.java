package WiDb;

public class AdapterWifiToBluetooth extends BluetoothConnection implements WiFi{

    @Override
    public void wifiConnect() {
        bluetoothConnect();
    }
}
