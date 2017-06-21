package info.androidhive.loginandregistration.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.loginandregistration.R;

public class StoreOrderActivity extends AppCompatActivity {
    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;
    private Dialog sure;
    private  Thread thread;
    private Socket clientSocket;
    private DataInputStream bw;
    private DataOutputStream br;
    private  String tmp;
    private String[] add=new String[50];
    private int number=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);
        setItems();
        setListener();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    void setItems() {
        // Array list for header
        ArrayList<String> header = new ArrayList<String>();
        // Array list for child items
        List<String> child1 = new ArrayList<String>();
        List<String> child2 = new ArrayList<String>();
        List<String> child3 = new ArrayList<String>();
        List<String> child4 = new ArrayList<String>();
        // Hash map for both header and child
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
        // Adding headers to list
        for (int i = 1; i < 5; i++) {
            header.add("Store" + i);
        }
        // Adding child data
        for (int i = 1; i < 5; i++) {
            child1.add("Store1  - " + " : order" + i);
        }
        // Adding child data
        for (int i = 1; i < 5; i++) {
            child2.add("Store2  - " + " : order" + i);
        }
        // Adding child data
        for (int i = 1; i < 5; i++) {
            child3.add("Store3  - " + " : order" + i);
        }
        // Adding child data
        for (int i = 1; i < 5; i++) {
            child4.add("Store4  - " + " : order" + i);
        }
        // Adding header and childs to hash map
        hashMap.put(header.get(0), child1);
        hashMap.put(header.get(1), child2);
        hashMap.put(header.get(2), child3);
        hashMap.put(header.get(3), child4);

        adapter = new ExpandableListAdapter(StoreOrderActivity.this, header, hashMap);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(adapter);
    }

    // Setting different listeners to expandablelistview
    void setListener() {

        // This listener will show toast on group click
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView listview, View view,
                                        int group_pos, long id) {
                Toast.makeText(StoreOrderActivity.this,
                        "You clicked : " + adapter.getGroup(group_pos),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // This listener will expand one group at one time
        // You can remove this listener for expanding all groups
        expandableListView
                .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    // Default position
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)

                            // Collapse the expanded group
                            expandableListView.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
                    }

                });

        // This listener will show toast on child click
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView listview, View view,
                                        int groupPos, int childPos, long id) {
                Toast.makeText(
                        StoreOrderActivity.this,
                        "You clicked : " + adapter.getChild(groupPos, childPos),
                        Toast.LENGTH_SHORT).show();
                tmp = adapter.getChild(groupPos, childPos).toString();
                sure = new Dialog(StoreOrderActivity.this);
                sure.setTitle("確定這個?");
                sure.setCancelable(false);
                sure.setContentView(R.layout.dialog);
                sure.show();
                return false;
            }
        });
    }
    public void cancel(View view) {
        sure.cancel();
    }
    public void sure(View view) {
        if (number > 9) {
            add[11]=null;
           // number--;
            Toast.makeText(StoreOrderActivity.this, "不能超過10份", Toast.LENGTH_SHORT).show();
        } else {
            number++;
            add[number] = tmp;
            Toast.makeText(
                    StoreOrderActivity.this,
                    "你點了" + tmp + "共" + number + "分",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void gotobill(View view) {
        Intent intent=new Intent();
        intent.setClass(StoreOrderActivity.this, Bill.class);
        Bundle bundle=new Bundle();
        bundle.putInt("passnumber",number);
        bundle.putStringArray("passstring",add);
        intent.putExtras(bundle);
        //intent.putExtra("passnumber",number);
        // intent.putExtra("passadd",add);
        startActivity(intent);
        finish();
    }
}
