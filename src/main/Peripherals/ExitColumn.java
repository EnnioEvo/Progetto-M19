package main.Peripherals;

import main.Manager;

public class ExitColumn extends Column {

    private Bar bar;

    public ExitColumn(String id, Manager man) {
        super(id, man);
        this.bar = bar;
    }

    public void exit(String carId){
        if (man.exit(carId) == true) {
            bar.open();
        }
        else{
            bar.close();
        }
    }
}
