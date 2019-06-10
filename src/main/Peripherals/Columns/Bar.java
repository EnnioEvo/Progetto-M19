package main.Peripherals.Columns;

public class Bar {
    private boolean isOpen;

    public Bar() {
        this.isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void open()
    {
        isOpen = true;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                    }
                }, 5000
        );
        close();
    }

    public void close(){
        isOpen = false;
    }

}



