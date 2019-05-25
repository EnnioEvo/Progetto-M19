package main.Peripherals;

import main.Manager;

public class ExitColumn extends Column {

    private Bar bar;

    public ExitColumn(String id) {
        super(id);
        this.bar = bar;
    }

    public void exit(String carId){
        Manager m = new Manager();
        if (m.exit(carId) == true) {
            bar.open();
        }
        else{
            bar.close();
        }
    }
}
