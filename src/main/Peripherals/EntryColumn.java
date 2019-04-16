package main.Peripherals;

import main.Floor;
import main.Manager;

import java.util.ArrayList;

public class EntryColumn extends Column {

    public EntryColumn(String id, int count, int countSub) {
        super(id, countSub, count);
    }

    public void entryTicket(String carId){
       count ++;
    }

    public void entrySub(String carId){
        countSub ++;
    }
}
