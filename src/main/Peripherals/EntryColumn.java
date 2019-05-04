package main.Peripherals;

import main.Floor;
import main.Manager;

import java.util.ArrayList;

public class EntryColumn extends Column {

    public EntryColumn(String id, Manager man) {
        super(id, man);
        this.man = man;
    }

    public void entryTicket(String carId){
        man.entryTicket(carId);
    }

    public void entrySub(String carId){
        man.entrySub(carId);
    }
}
