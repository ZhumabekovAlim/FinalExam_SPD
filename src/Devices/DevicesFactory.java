package Devices;

public class DevicesFactory {
    public static Device action(int type){
        switch (type) {
            case 1:
                return new AirConditioner(15);
            case 2:
                return new Camera(3);
            case 3:
                return new CoffeeMachine(5);
            case 4:
                return new Door(1);
            case 5:
                return new Kettle(7);
            case 6:
                return new Light(2);
            case 7:
                return new Mirror(4);
            case 8:
                return new TemperatureSensor(8);
            case 9:
                return new VacuumCleaner(9);
            case 10:
                return new WashingMachine(16);

            default:
                throw new IllegalArgumentException("Not this devices in your home!");
        }
    }
}
