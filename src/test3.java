import main.Driver;
import main.Manager;

public class test3 {
    public static void main(String[] args) {

        Manager m = new Manager();
        int posti[] = new int[10];
        m.makeFloors(7, 50);
        m.setTariff(5);
        m.setSpacesSubdivision(50);

        Driver d = new Driver("gggggg");
        m.entryTicket(d.getCarId());
        m.entryTicket(d.getCarId());


    }
}
