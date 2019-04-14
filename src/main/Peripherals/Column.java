package main.Peripherals;

public abstract class Column {
    protected String id;
    protected static int count = 0;
    protected int countSub;

    public Column(String id, int count, int countSub) {
        this.id = id;
        this.count = count;
        this.countSub = countSub;
    }
}
