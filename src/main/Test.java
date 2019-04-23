package main;

public class Test {

    public static void main(String[] args) {

        int posti[] = new int[10];
        Manager m = new Manager();
        m.makeFloors(7, 50);
        m.setTariff(5);
        m.setSpacesSubdivision(50);
        m.entryTicket("IT4560JV");
        m.entrySub("IT3456GT");
       // m.Analytics();

        Subscription s = new Subscription("ciao");
        System.out.println(s);
    }
}

