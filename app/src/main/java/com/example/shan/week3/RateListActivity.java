package com.example.shan.week3;

import android.app.ListActivity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{

    private String[] list_data={"one","two","three","four"};
    private int msgWhat=3;
    Handler handler;
//    存放文字，汇率
    private ArrayList<HashMap<String,String>> listItems;
//    适配器
    private SimpleAdapter listItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
///       setContentView(R.layout.activity_rate_list);

        List<String > list1=new ArrayList<String>();
        for(int i=0;i<100;i++){
            list1.add("item"+i);
        }

        ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list_data);
        setListAdapter(adapter);



        //开启子线程
        Thread t=new Thread( this);
        t.start();

        handler=new Handler(){
          public void handleMessage(Message msg){
              if (msg.what==5){
                  List<String> retList=(List<String>)msg.obj;
                  ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,retList);
                  setListAdapter(adapter);

                  Log.i("handler","reset list...");
              }
              super.handleMessage(msg);
          }
        };
    }

    public void run(){
        //获取网络数据，放入List带回
        Log.i("thread","run……");
        List<String> rateList=new ArrayList<String>();
        try{
            Document doc= Jsoup.connect("http://www.usd-cny.com/icbc.htm").get();
            Elements tbs=doc.getElementsByClass("tableDataTable");
            Element table=tbs.get(0);

            Elements tds=table.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=5){
                Element td=tds.get(i);
                Element td2=tds.get(i+3);

                String tdStr=td.text();
                String pStr=td2.text();
                rateList.add(tdStr+"==>"+pStr);
                Log.i("td",tdStr+"==>"+pStr);
            }

        }catch (MalformedURLException e){
            Log.e("www",e.toString());
            e.printStackTrace();
        }catch (IOException e){
            Log.e("www",e.toString());
            e.printStackTrace();
        }

        //获取Msg对象，用于返回主线程
        Message msg=handler.obtainMessage(5);
        msg.obj=rateList;
        handler.sendMessage(msg);

        Log.i("thread","sendMessage……");
    }

    public void initListView(){
        listItems=new ArrayList<HashMap<String,String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate： " + i); // 标题文字
            map.put("ItemDetail", "detail" + i); // 详情描述
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems
                R.layout.activity_rate_list, // ListItem的XML布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
        );
    }
}
