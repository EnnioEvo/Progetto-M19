package main.Peripherals.Cash;

public class VisaAdapter implements PaymentAdapter {
    @Override
    public Boolean pay(Double amount) {
        if ((int)(Math.random()*4) > 0){
            return Boolean.TRUE; //3 volte su 4 il pagamento riesce
        }
        return Boolean.FALSE;
    }

    @Override
    public String getName() {
        return "Visa";
    }
}
