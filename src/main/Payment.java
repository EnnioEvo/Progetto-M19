package main;

public class Payment {
    Double amount;
    String carid;
    Boolean check;

    public Payment(Double amount, String carid, Boolean check){
        this.amount = amount;
        this.carid = carid;
        this.check = check;
    }

    public Boolean getCheck() { return check; }

    public Double getAmount() { return amount; }

    public String getCarid() { return carid; }

    public void setCheck(Boolean check) { this.check = check;  }
}
