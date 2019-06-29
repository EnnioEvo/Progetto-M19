package main.Peripherals.Columns;

public class Bar {
    private boolean isOpen;

    //Costruttore della classe Bar
    public Bar() {
        this.isOpen = false;
    }

    //Restituisce lo stato della sbarra
    public boolean isOpen() {
        return isOpen;
    }

    //Imposta lo stato della sbarra ad "aperta"
    public void setOpen(boolean open) {
        isOpen = open;
    }

    //Apre la sbarra, dopo un certo tempo la sbarra si chiude
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

    //Chiude la sbarra
    public void close(){
        isOpen = false;
    }

}



