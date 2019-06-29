package main.Peripherals.Cash;

public class Payment {
    Double amount;
    Double amountPaid;
    Double dovuto;
    String carId;
    boolean check;


    public Payment(Double amount, String carId, boolean check){
        this.amount = amount;
        this.dovuto = amount;
        this.carId = carId;
        this.check = check;
        this.amountPaid = 0.;
    }

    public Boolean getCheck() { return check; }

    public Double getAmount() { return amount; }

    public Double getDovuto() {
        return dovuto;
    }

    public String getCarid() { return carId; }

    public void setCheck(Boolean check) { this.check = check;  }

    public void setAmount(Double amount) {
        this.amount = amount;
        this.dovuto = amount;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
        this.dovuto = this.amount - amountPaid;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }
}
