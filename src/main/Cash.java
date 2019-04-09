package main;

public class Cash{
    private int id;
    private double tariffaOraria; // in Euro
    private double fund = 0;

    public Cash(int id, double tariffaOraria){
        this.id = id;
        this.tariffaOraria = tariffaOraria;
    }

    public double payWithCash(String carId){
        int nOreServizio = (int)(Math.random()*29) + 1; // verrà sostituito dalle ore effettive
        return nOreServizio * tariffaOraria;
    }

    public double getMoney(double moneyPut, double notYetPaid){
        fund += moneyPut;
        return notYetPaid - moneyPut;
    }
}