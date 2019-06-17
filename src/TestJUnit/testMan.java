package TestJUnit;
import Exceptions.SubdivisionException;
import main.Manager.Manager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class testMan {

    TestManager man = new TestManager();

    @Test
    public void testMakeFloors() {
        int floorsSize;
        floorsSize = man.makeFloors(5, 200); // ho 1000 posti totali
        assertEquals(5, floorsSize);
    }

    @Test //corretto
    public void testSpaceSubDivision() {
        man.makeFloors(5, 200); // ho 1000 posti totali
        int numberSubSpace;
        numberSubSpace = man.setSpacesSubdivision(200); // ho assegnato 200 posti agli abbonati e 800 ai tickets
        assertEquals(200,numberSubSpace);
    }

    @Test
    public void testFailSpaceSubDivision(){
        man.makeFloors(1, 10); // ho 10 posti totali
        int numberSubSpace;
        numberSubSpace = man.setSpacesSubdivision(200); // vorrei assegnare 200 posti agli abbonati: IMPOSSIBILE!
        assertEquals(0 ,numberSubSpace);
    }

    @Test
    public void testEntryTicket(){
        String s;
        man = new TestManager();
        man.makeFloors(5, 200); // ho 1000 posti totali
        man.setSpacesSubdivision(200);
        // ingresso valido
        s = man.entryTicket("IT4567IO");
        assertEquals("entryOk",s);
    }

    @Test
    public void testEntryTicketFail(){ // fallisce per targa sintatticamente errata
        String s;
        man = new TestManager();
        man.makeFloors(5, 200); // ho 1000 posti totali
        man.setSpacesSubdivision(200);
        // ingresso valido
        s = man.entryTicket("IT4567");
        assertEquals("entryNo-- targa non valida",s);
    }

}
