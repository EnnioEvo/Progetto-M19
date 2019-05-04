package main.Peripherals;

public class Bar {
    private boolean isOpen;

    public Bar(boolean isOpen) {
        this.isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void open(){
        isOpen = true;
    }

    public void close(){
        isOpen = false;
    }
}
