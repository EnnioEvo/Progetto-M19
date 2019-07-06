package main.Utilities;

import main.Parking.DataBase.DataBaseAdapter;
import main.Peripherals.Cash.PaymentAdapter;

public class ServiceFactory
{
    static ServiceFactory instance;
    private DataBaseAdapter dataBaseAdapter;
    private PaymentAdapter paymentAdapter;

    private ServiceFactory() {}

    // Metodo statico per ottenere l'istanza
    public static synchronized ServiceFactory getInstance()
    {
        if (instance == null)
        {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public DataBaseAdapter getDataBaseAdapter(String filepath)
    {
        if (dataBaseAdapter == null)
        {
            String className = System.getProperty("db.class.name");
            try
            {
                dataBaseAdapter = (DataBaseAdapter) Class.forName(className).getDeclaredConstructor(String.class).newInstance(filepath);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return dataBaseAdapter;
    }

    public PaymentAdapter getPaymentAdapter()
    {
        if (paymentAdapter == null)
        {
            String className = System.getProperty("payement.class.name");
            try
            {
            paymentAdapter = (PaymentAdapter) Class.forName(className).getDeclaredConstructor().newInstance();
            }
                catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return paymentAdapter;
    }
}
