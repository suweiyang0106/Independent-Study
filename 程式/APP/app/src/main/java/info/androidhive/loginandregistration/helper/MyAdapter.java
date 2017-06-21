package info.androidhive.loginandregistration.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.Order;

/**
 * Created by Aspire on 2017/1/9.
 */

public class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<Order> order;

    public MyAdapter(Context context, List<Order> order){
        myInflater = LayoutInflater.from(context);
        this.order = order;
    }
    @Override
    public int getCount() {
        return order.size();
    }

    @Override
    public Object getItem(int i) {
        return order.get(i);
    }

    @Override
    public long getItemId(int i) {
        return order.indexOf(getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view==null){
            view = myInflater.inflate(R.layout.listview_cell2, null);
            TextView order=(TextView)view.findViewById(R.id.textView);
            TextView order_number=(TextView)view.findViewById(R.id.textView2);
            TextView order_complete=(TextView)view.findViewById(R.id.textView3);
            order.setText(this.order.get(i).getOrder());
            order_number.setText(this.order.get(i).getOrder_numbero());
            order_complete.setText(this.order.get(i).getOrder_complete());

        }
        return  view;
    }
}
class ViewHolder {
    TextView order;
    TextView order_number;
    TextView order_complete;
    public ViewHolder(TextView order, TextView order_number, TextView order_complete){
        this.order = order;
        this.order_number = order_number;
        this.order_complete=order_complete;
    }
}
