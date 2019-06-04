package main.Manager;

public class Floor {


    private int freeSpace;
    private int id;
    private int countCarIn;

    public Floor(int id, int freeSpace)
    {
        this.id = id;
        this.freeSpace = freeSpace;
        this.countCarIn = 0;
    }

    public String getFloorInfo()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Piano ");
        sb.append(getId());
        sb.append(", posti ");
        sb.append(getFreeSpace());
        sb.append(", occupati ");
        sb.append(getCountCarIn());
        return sb.toString();
    }


    public void addCar()
    {
        countCarIn++;
    }

    public void deleteCar()
    {
        countCarIn--;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getFreeSpace()
    {
        return freeSpace;
    }

    public int getId()
    {
        return id;
    }

    public int getCountCarIn()
    {
        return countCarIn;
    }
}
