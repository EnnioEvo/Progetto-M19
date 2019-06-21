package JUnitTest;

import static org.junit.Assert.*;

import Exceptions.SubdivisionException;
import main.Manager.Driver;
import org.junit.Test;
import main.Manager.Driver.*;
import main.Manager.Subscriptions.*;

public class TestEntryTicket {

    TestManager m = new TestManager();

    @Test
    //test valido
    public void testMakeFloors(){
        m.makeFloors(10,200);
        assertEquals(2000, m.getFreeSpacesTot());
    }

    @Test
    //test valido
    public void testRemoveFloor(){
        m.makeFloors(10,200);
        m.removeFloor(1);
        assertEquals(9, m.getFloorsList().size());
    }

    @Test
    // provo ad eliminare un piano che non esiste
    public void testRemoveFloorFail(){
        m.makeFloors(10,200);
        m.removeFloor(12);
        assertEquals(10, m.getFloorsList().size());
    }

    @Test
    // test valido posti validi
    public void testSetSpaceSub(){
        m.makeFloors(10,200);
        m.setSpacesSubdivision(200);
        assertEquals(200,m.getFreeSpacesSubTot());
    }

    @Test
    // test fail posti di piu di quanti ne esistono
    (expected = SubdivisionException.class)
    public void testSetSpaceSub2(){
        m.makeFloors(1,200);
        m.setSpacesSubdivision(500);
    }

    @Test
    //ingresso valido, targa valida
    public void entryTicket1(){
        String s;
        m.makeFloors(10,200);
        s = m.entryTicket("00000000");
        assertEquals("entryOk--Ingresso riuscito, IDTicket:   00000000, " +
                "ora Ingresso:    00:00:00:000", s);
    }

    @Test
    // targa non valida, test sintattico
    public void entryTicket2(){
        String s;
        m.makeFloors(10,200);
        s = m.entryTicket("0000000000");
        assertEquals("entryNo--Targa non valida.", s);
    }

    @Test
    // posti ticket finiti
    public void entryTicket3(){
        String s;
        m.makeFloors(1,1);
        m.setSpacesSubdivision(0);
       //faccio entrare un driver
        m.entryTicket("00000000");
        // quest'ingresso genera errore
        s = m.entryTicket("00000001");
        assertEquals("entryNo--Posti ticket finiti.", s);
    }

    @Test
    // targa gia presente nel parcheggio
    public void entryTicket4(){
        String s;
        m.makeFloors(10,10);
        m.setSpacesSubdivision(0);
        //faccio entrare un driver
        m.entryTicket("00000000");
        // quest'ingresso genera errore
        s = m.entryTicket("00000000");
        assertEquals("entryNo--Ingresso fallito: targa: " +
                "00000000 già presente all'interno del parcheggio.", s);
    }





}
