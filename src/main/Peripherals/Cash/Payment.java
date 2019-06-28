package main.Peripherals.Cash;

public class Payment {
    Double amount;
    String carId;
    Boolean check;


    public Payment(Double amount, String carId, Boolean check){
        this.amount = amount;
        this.carId = carId;
        this.check = check;
    }

    public Boolean getCheck() { return check; }

    public Double getAmount() { return amount; }

    public String getCarid() { return carId; }

    public void setCheck(Boolean check) { this.check = check;  }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }
}
