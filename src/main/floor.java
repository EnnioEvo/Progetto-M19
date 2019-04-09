package main;

public class floor {


    int freeSpace;
    int id;
    int countCarIn;

    public floor(int id,int freeSpace) {
        this.id = id;
        this.freeSpace = freeSpace;
        this.countCarIn = 0;
    }

    public void addCar(){
        countCarIn++;
    }

    public void deleteCar(){
        countCarIn--;
    }

    public void setId(int id) {
        this.id = id;
    }
}
