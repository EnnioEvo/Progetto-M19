package main.Peripherals;

public interface Peripheral
{
    //Permette il passaggio di informazioni tra il Manager e la colonnina, in modo da
    //aggironare la GUI
    public void receiveInfo(String info);

    //Viene utilizzato per eseguire il metodo update() nell'observer, ogni volta che
    //il contenuto di variabili presenti nella GUI cambia
    public void notifyObs();

    //Ottiene l'ID
    public void getIdFromMan();
}
