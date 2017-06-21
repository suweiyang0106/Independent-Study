package info.androidhive.loginandregistration.helper;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.androidhive.loginandregistration.R;

/**
 * Created by Aspire on 2016/10/31.
 */

public class ListView_Adapter  extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<Get_Order_Infor> get_order_infors;

    public  ListView_Adapter(Context context, List<Get_Order_Infor> get_order_infors)
    {
        myInflater=LayoutInflater.from(context);
        this.get_order_infors=get_order_infors;
    }
    @Override
    public int getCount()
    {
        return get_order_infors.size();
    }
    @Override
    public Object getItem(int arg0) {
        return get_order_infors.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return get_order_infors.indexOf(getItem(position));
    }

    @Override
    public View getView(int poisiton, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.listview_cell, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.dish),
                    (TextView) convertView.findViewById(R.id.amount)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Get_Order_Infor get_order_infor=(Get_Order_Infor)getItem(poisiton);
        holder.dish.setText(get_order_infor.getDish());
        holder.amount.setText( Integer.toString(get_order_infor.getAmount()));
        holder.dish.setTextSize(25);
        holder.amount.setTextSize(25);
        holder.dish.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        holder.amount.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        return  convertView;
    }
    private class ViewHolder {
        TextView dish;
        TextView amount;
        public ViewHolder(TextView txtTitle, TextView txtTime){
            this.dish = txtTitle;
            this.amount = txtTime;
        }
    }
}
