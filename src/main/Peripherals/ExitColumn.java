package main.Peripherals;

public class ExitColumn extends Column {
    private int countMaxEntry;
    private int countMaxSub;

    public ExitColumn(String id, int count, int countSub, int countMaxEntry, int countMaxSub) {
        super(id, count, countSub);
        this.countMaxEntry = countMaxEntry;
        this.countMaxSub = countMaxSub;
    }

    public boolean exitTicket(String carId){
        count --;
        return true;
    }

    public boolean exitSub(String carId){
        countSub --;
        return true;
    }
}
