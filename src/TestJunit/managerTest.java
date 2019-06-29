package TestJunit;


import Exceptions.SubdivisionException;
import main.Manager.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class managerTest {

    Manager m = new Manager();

    @Test
    public void makeFloorsTest(){
        m.makeFloors(5,200);
        assertEquals(1000,m.getFreeSpacesTot());
    }

    @Test
    public void removeFloorTest(){
        m.makeFloors(5,200);
        m.removeFloor(1);
        assertEquals(4,m.getFloorsList().size());
    }

    @Test
    public void setSpaceSubDivisionTest(){
        m.makeFloors(5,200);
        m.setFreeSpacesSubNow(200);
        assertEquals(200,m.getFreeSpacesSubNow());
    }


   /* @Test
    public void setSpaceSubDivisionTest2() {
        m.makeFloors(1, 200);
        SubdivisionException ex =
                assertThrows(SubdivisionException.class, () ->
                        m.setFreeSpacesSubNow(500),"Non ci sono abbastanza posti");

    }*/
    @Test
   public void setSpaceSubDivisionTest2(){
       m.makeFloors(1,200);
       try{
           m.setFreeSpacesSubNow(500);
       }catch (SubdivisionException ex){
           assertEquals("Non ci sono abbastanza posti", ex);
       }
    }

}
