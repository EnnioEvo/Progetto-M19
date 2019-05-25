package main.Peripherals;

import main.Manager;

public class EntryColumn extends Column {
    public EntryColumn(String id) {
        super(id);
    }

    public void entryTicket(String id){
        Manager m = new Manager();
        m.entryTicket(id);
    }

    public void entrySub(String id){
        Manager m = new Manager();
        m.entrySub(id);
    }
}
