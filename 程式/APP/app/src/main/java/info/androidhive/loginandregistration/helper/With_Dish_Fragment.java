package info.androidhive.loginandregistration.helper;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.activity.Bill_Check;

/**
 * Created by Aspire on 2016/10/23.
 */

public class With_Dish_Fragment extends Fragment {
    CheckBox check1,check2,check3,check4,check5,check6;
    int numberOfCheckboxesChecked = 0;
    Button check_out;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.with_dish,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        check_out=(Button)getView().findViewById(R.id.button3) ;
        check1=(CheckBox)getView().findViewById(R.id.checkBox6);
        check2=(CheckBox)getView().findViewById(R.id.checkBox7);
        check3=(CheckBox)getView().findViewById(R.id.checkBox8);
        check4=(CheckBox)getView().findViewById(R.id.checkBox9);
        check5=(CheckBox)getView().findViewById(R.id.checkBox10);
        check6=(CheckBox)getView().findViewById(R.id.checkBox11);
        check1.setText("炒高麗菜");
        check2.setText("炒地瓜葉");
        check3.setText("番茄炒蛋");
        check4.setText("木耳炒芹菜");
        check5.setText("滷蛋");
        check6.setText("肉絲炒青椒");
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && numberOfCheckboxesChecked > 2) {
                    check1.setChecked(false);
                } else {
                    // the checkbox either got unchecked
                    // or there are less than 2 other checkboxes checked
                    // change your counter accordingly
                    if (isChecked) {
                        numberOfCheckboxesChecked++;
                    } else {
                        numberOfCheckboxesChecked--;
                    }
                    // now everything is fine and you can do whatever
                    // checking the checkbox should do here
                }
            }
        });
        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && numberOfCheckboxesChecked > 2) {
                    check2.setChecked(false);
                } else {
                    // the checkbox either got unchecked
                    // or there are less than 2 other checkboxes checked
                    // change your counter accordingly
                    if (isChecked) {
                        numberOfCheckboxesChecked++;
                    } else {
                        numberOfCheckboxesChecked--;
                    }
                    // now everything is fine and you can do whatever
                    // checking the checkbox should do here
                }
            }
        });
        check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && numberOfCheckboxesChecked > 2) {
                    check3.setChecked(false);
                } else {
                    // the checkbox either got unchecked
                    // or there are less than 2 other checkboxes checked
                    // change your counter accordingly
                    if (isChecked) {
                        numberOfCheckboxesChecked++;
                    } else {
                        numberOfCheckboxesChecked--;
                    }
                    // now everything is fine and you can do whatever
                    // checking the checkbox should do here
                }
            }
        });
        check4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && numberOfCheckboxesChecked > 2) {
                    check4.setChecked(false);
                } else {
                    // the checkbox either got unchecked
                    // or there are less than 2 other checkboxes checked
                    // change your counter accordingly
                    if (isChecked) {
                        numberOfCheckboxesChecked++;
                    } else {
                        numberOfCheckboxesChecked--;
                    }
                    // now everything is fine and you can do whatever
                    // checking the checkbox should do here
                }
            }
        });
        check5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && numberOfCheckboxesChecked > 2) {
                    check5.setChecked(false);
                } else {
                    // the checkbox either got unchecked
                    // or there are less than 2 other checkboxes checked
                    // change your counter accordingly
                    if (isChecked) {
                        numberOfCheckboxesChecked++;
                    } else {
                        numberOfCheckboxesChecked--;
                    }
                    // now everything is fine and you can do whatever
                    // checking the checkbox should do here
                }
            }
        });
        check6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && numberOfCheckboxesChecked > 2) {
                    check6.setChecked(false);
                } else {
                    // the checkbox either got unchecked
                    // or there are less than 2 other checkboxes checked
                    // change your counter accordingly
                    if (isChecked) {
                        numberOfCheckboxesChecked++;
                    } else {
                        numberOfCheckboxesChecked--;
                    }
                    // now everything is fine and you can do whatever
                    // checking the checkbox should do here
                }
            }
        });
       check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                System.out.println("有小菜");
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