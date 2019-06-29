package main.Peripherals;

public interface Peripheral
{
    //Riceve informazioni dal client e individua il comando associato alla HashMap
    // e lo esegue utilizzando come argomenti quelli passati dal client
    public void receiveInfo(String info);

    //Richiede all'observer di aggiornare lo stato delle variabili sull'interfaccia grafica
    public void notifyObs();

    //Ottiene l'ID
    public void getIdFromMan();
}
