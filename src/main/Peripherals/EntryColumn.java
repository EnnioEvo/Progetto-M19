package main.Peripherals;

public class EntryColumn extends Column {
    public EntryColumn(String id, int count, int countSub) {
        super(id, count, countSub);
    }

    public void entryTicket (String carId){
       count --;
    }
}
