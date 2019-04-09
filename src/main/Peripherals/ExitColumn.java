package main.Peripherals;

public class ExitColumn extends Column {
    public ExitColumn(String id, int count, int countSub) {
        super(id, count, countSub);
    }

    public void exitTicket  (){
        count ++;
    }

    public void exitSub (){

    }
}
