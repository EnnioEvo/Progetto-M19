package main.Peripherals.Columns;

import main.Manager.Manager;
import main.Peripherals.Observer;

public class ExitColumn extends Column {

    private Bar bar;
    private Observer obs;

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

    @Override
    public void notifyObs()
    {
        obs.update();
    }

    public void setObs(Observer obs)
    {
        this.obs = obs;
    }
}
