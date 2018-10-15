package com.example.shan.week3;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

public class MyListActivity extends AppCompatActivity {

    private final String TAG="Mylist";
    String data[]={"wait……"};
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        GridView listView=(GridView)findViewById(R.id.mylist);
//        for(int i=0;i<100;i++){
//            data.add("item"+i);
//        }

        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.nodata));
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

    }
}
