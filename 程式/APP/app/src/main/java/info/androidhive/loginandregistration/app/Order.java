package info.androidhive.loginandregistration.app;

/**
 * Created by Aspire on 2017/1/9.
 */

public class Order {
    private int type;
    private String name;
    private String time;
    private String order;
    private String order_number;
    private String order_complete;
    public Order(String order,String order_number,String order_complete) {
        this.order=order;
        this.order_number=order_number;
        this.order_complete=order_complete;
    }
    public  String getOrder()
    {
        return  order;
    }
    public void setOrder(String order)
    {
        this.order=order;
    }
    public String getOrder_numbero()
    {
        return order_number;
    }
    public void setOrder_number(String order_number)
    {
        this.order_number = order_number;
    }
    public String getOrder_complete()
    {
        return  order_complete;
    }
    public void setOrder_complete(String order_complete)
    {
        this.order_complete=order_complete;
    }
}
