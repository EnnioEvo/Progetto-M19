package main.Peripherals;

public abstract class Column {
    protected String id;
    protected int count;
    protected int countSub;

    public Column(String id, int count, int countSub) {
        this.id = id;
        this.count = count;
        this.countSub = countSub;
    }
}
