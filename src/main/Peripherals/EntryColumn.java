package main.Peripherals;

import main.Floor;
import main.Manager;

import java.util.ArrayList;

public class EntryColumn extends Column {
    private int countMaxEntry;
    private int countMaxSub;

    public EntryColumn(String id, int count, int countSub, int countMaxEntry, int countMaxSub) {
        super(id, countSub, count);
        this.countMaxEntry = countMaxEntry;
        this.countMaxSub = countMaxSub;
    }

    public boolean entryTicket(String carId){
       if(count >= countMaxEntry){
            return false;
       }
       count ++;
       return true;
    }

    public boolean entrySub(String carId){
        if(countSub >= countMaxSub){
            return false;
        }
        countSub ++;
        return true;
    }
}
