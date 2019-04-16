package main;

import java.util.ArrayList;

public class Manager
{

    private ArrayList<Floor> floorsList;


    public Manager() {
        floorsList = new ArrayList<>();

    }


    public void makeFloors(int[] freeSpaces)
    {
        for(int i=0; i<freeSpaces.length; i++)
        {
            Floor floor = new Floor(floorsList.size(), freeSpaces[i]);
            floorsList.add(floor);
        }
    }
}
