package main;

public class floor {


    int freeSpace;
    int id;
    int countCarIn;

    public floor(int freeSpace) {
        this.freeSpace = freeSpace;
        this.countCarIn = 0;
    }

    public void addCar(){
        countCarIn++;
    }

    public void deleteCar(){
        countCarIn--;
    }




}
