package info.androidhive.loginandregistration.helper;

/**
 * Created by Aspire on 2016/10/31.
 */
public class Get_Order_Infor {
    private  String dish;
    private  int amount;
    public  Get_Order_Infor(String dish,int amount)
    {
        this.dish=dish;
        this.amount=amount;
    }
    public  int getAmount()
    {
        return  amount;
    }
    public String getDish()
    {
        return  dish;
    }
    public void setAmount( int amount)
    {
        this.amount=amount;
    }
    public void setDish(String dish)
    {
        this.dish=dish;
    }
}