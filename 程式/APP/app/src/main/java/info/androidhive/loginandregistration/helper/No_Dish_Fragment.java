package info.androidhive.loginandregistration.helper;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.activity.Bill_Check;


public class No_Dish_Fragment extends Fragment {
    Button check;
    Context mcontext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.no_dish,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        check=(Button)getView().findViewById(R.id.go_check) ;

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("沒有小菜");
                if(check_amount()<=5&&check_amount()>0)
                {
                    Intent intent=new Intent(getActivity(), Bill_Check.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(),"請介於一到五樣之間",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
public int check_amount()
{
    MyDBHelper controller=new MyDBHelper(getActivity(),"",null,1);;
    Cursor cursor =controller.list_all();
    int amount=0;
    if(cursor!=null&&cursor.getCount()>=0)
    {
    while (cursor.moveToNext())
    {
        amount=amount+cursor.getInt(3);
    }
    }
    return  amount;
}

}