package main;

import java.util.ArrayList;

public class Manager
{

    private ArrayList<floor> floorsList;


    public Manager() {
        floorsList = new ArrayList<>();

    }

    public void makeFloors(int[] freeSpaces)
    {
        for(int i=0; i<freeSpaces.length; i++)
        {
            floor floor = new floor(floorsList.size(), freeSpaces[i]);
            floorsList.add(floor);
        }
    }
}
