package main.Peripherals;

import main.Manager;

public class ExitColumn extends Column {

    private Bar bar;

    public ExitColumn(String id, Manager man)
    {
        super(id, man);
        bar = new Bar();
    }

    public void exit(String carId)
    {
        if (man.exit(carId))
        {
            bar.open();
        }
    }
}
