package com.example.shan.week3;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.widget.AdapterView.*;

public class RateListActivity2 extends ListActivity implements Runnable, OnItemLongClickListener, OnItemClickListener {

    private static final String TAG = "RateList2";
    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、
    private SimpleAdapter listItemAdapter; // 适配器
    private int msgWhat=7;
    private List<HashMap<String, String>> retList;
    SimpleAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       setContentView(R.layout.activity_rate_list);

        initListView();
        this.setListAdapter(listItemAdapter);

        //开启子线程
        Thread t=new Thread( this);
        t.start();

        handler=new Handler(){
            public void handleMessage(Message msg) {
            if(msg.what==7){
                retList= (List<HashMap<String, String>>)msg.obj ;
                adapter = new SimpleAdapter(RateListActivity2.this, retList,
//                listItemAdapter = new SimpleAdapter(RateListActivity2.this, retList,
                        R.layout.list_item, // ListItem的XML布局实现
                        new String[] { "ItemTitle", "ItemDetail" },
                        new int[] { R.id.itemTitle, R.id.itemDetail });
                setListAdapter(adapter);
//                setListAdapter(listItemAdapter);
                Log.i("handler","reset list...");
            }
            super.handleMessage(msg);
            }
        };

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

    }

    @Override
    public void run() {
        Log.i("thread","run.....");
        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();
        try {
            Document doc = Jsoup.connect("http://www.usd-cny.com/icbc.htm").get();
            Elements tbs = doc.getElementsByClass("tableDataTable");
            Element table = tbs.get(0);
            Elements tds = table.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i+=5) {
                Element td = tds.get(i);
                Element td2 = tds.get(i+3);
                String tdStr = td.text();
                String pStr = td2.text();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", tdStr);
                map.put("ItemDetail", pStr);
                rateList.add(map);
                Log.i("td",tdStr + "=>" + pStr);
            }
            marker = true;
        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }

        Message msg=handler.obtainMessage();
        msg.what = 7;
//        if(marker){
//            msg.arg1 = 1;
//        }else{
//            msg.arg1 = 0;
//        }
        msg.obj = rateList;
        handler.sendMessage(msg);
        Log.i("thread","sendMessage.....");


    }

    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate： " + i); // 标题文字
            map.put("ItemDetail", "detail" + i); // 详情描述
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems
                R.layout.list_item, // ListItem的XML布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
        );
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Object itemAtPosition=getListView().getItemAtPosition(position);
        HashMap<String,String> map=(HashMap<String,String>) itemAtPosition;
        String titleStr=map.get("ItemTitle");
        String detailStr=map.get("ItemDetail");
        Log.i(TAG,"onItemClick:titleStr"+titleStr);
        Log.i(TAG,"onItemClick:detailStr"+detailStr);

        TextView title=(TextView)view.findViewById(R.id.itemTitle);
        TextView detail=(TextView)view.findViewById(R.id.itemDetail);
        String title2=String.valueOf(title.getText());
        String detail2=String.valueOf(detail.getText());
        Log.i(TAG,"onItemClick:title2="+title2);
        Log.i(TAG,"onItemClick:detail2="+detail2);

        //打开新的页面，传入参数
        Intent rateCalc=new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateCalc);
    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG,"onItemLongClick:长按列表选项position="+position);
        //删除操作
        //构造对话框进行确认
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"onClick:对话框事件处理");
                //删除数据项
                retList.remove(position);
                //更新适配器
                adapter.notifyDataSetChanged();
//                listItemAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);
        builder.create().show();
        Log.i(TAG,"onItemLongClick:size"+listItems.size());
//        retList.remove(position);
//        listItemAdapter.notifyDataSetChanged();
        return true;
    }

}
