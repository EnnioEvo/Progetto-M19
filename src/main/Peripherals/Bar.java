package main.Peripherals;

public class Bar {
    private boolean isOpen;

    public Bar(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void open(){
        isOpen = true;
    }

    public void close(){
        isOpen = false;
    }
}
