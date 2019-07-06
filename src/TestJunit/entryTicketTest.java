package TestJunit;
import main.Parking.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class entryTicketTest {

    Parking m = new Parking();
    Entrance em = new Entrance(m);
   splitString split = new splitString();

    @Test
    //Test targa non valida
    public void entryTicketTest(){
        m.makeFloors(1,200);
        m.setSpacesSubdivision(100);
        assertEquals("entryNo--Targa non valida.",em.entryTicket("000000000"));
    }

    @Test
    //Targa valida
    public void invalidCarID(){
        String s="", st;
        m.makeFloors(1,200);
        m.setSpacesSubdivision(100);
        //Sono costretto a far lo split della stringa che mi conferma l'ingresso perchè i millisecondi della data non
        //Davano la possibilita di convalidare il tes
        s = split.split(em.entryTicket("0000000"));
        assertEquals("entryOk", s);
    }
    @Test
    //Posti finiti
    public void finishedticketSpace(){
        m.makeFloors(1,1);
        m.setSpacesSubdivision(0);
        //Creo un ingresso valido
        em.entryTicket("0000000");
        //secondo ingresso che genera errore
        assertEquals("entryNo--Posti ticket finiti.", em.entryTicket("1111111"));
    }

    @Test
    //Targa in ingresso uguale
    public void sameCarID(){
        m.makeFloors(1,200);
        m.setSpacesSubdivision(100);
        //Creo un ingresso valido
        em.entryTicket("0000000");
        //Secondo ingresso con stessa targa
        assertEquals("entryNo--Ingresso fallito: targa: 0000000 già presente all'interno del parcheggio.",
                em.entryTicket("0000000"));
    }



}
