package com.example.shan.week3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RateActivity extends AppCompatActivity implements Runnable{

    private final String TAG="Rate";
    private float dollarRate;
    private float euroRate;
    private float wonRate;
    EditText rmb;
    TextView show;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=(EditText)findViewById(R.id.rmb);
        show=(TextView) findViewById(R.id.show);
        Log.i("aaa", "bbb");

        SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);

//        dollarRate=sharedPreferences.getFloat("dollar_rate",0.0f);
//        euroRate=sharedPreferences.getFloat("euro_rate",0.0f);
//        wonRate=sharedPreferences.getFloat("won_rate",0.0f);
        String time=sharedPreferences.getString("update_date","2");

        Log.i(TAG,"onCreate:sp dollarRate="+dollarRate);
        Log.i(TAG,"onCreate:sp euroRate="+euroRate);
        Log.i(TAG,"onCreate:sp wonRate="+wonRate);
        Log.i(TAG,"onCreate:sp update_date="+time);

        //获取当前时间
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
        String time_now=(String )sd.format(new Date());

        //判断上次更新时间与当前时间是否一致，一致则不更新，直接使用
        if(time_now.equals(time)){
            //时间一致，直接使用
            dollarRate=sharedPreferences.getFloat("dollar_rate",0.0f);
            euroRate=sharedPreferences.getFloat("euro_rate",0.0f);
            wonRate=sharedPreferences.getFloat("won_rate",0.0f);
        }else{
            //时间不一致，通过网络更新

            //开启子线程
            Thread t=new Thread(this);
            t.start();

//        handler = new Handler(){
//            public void handleMessage(Message msg){
//                if(msg.what==5){
//                    String str=(String) msg.obj;
//                    Log.i(TAG,"handleMessage:getMessage msg ="+str);
//                    show.setText(str);
//                }
//                super.handleMessage(msg);
//            }
//        };

            handler=new Handler(){
                public void handleMessage(Message msg){
                    if(msg.what==5){
                        Bundle bd1=(Bundle)msg.obj;
                        dollarRate=bd1.getFloat("dollar-rate");
                        euroRate=bd1.getFloat("euro-rate");
                        wonRate=bd1.getFloat("won-rate");
                        String time=bd1.getString("update-date");

                        Log.i(TAG, "handleMessage: dollarRate:" + dollarRate);
                        Log.i(TAG, "handleMessage: euroRate:" + euroRate);
                        Log.i(TAG, "handleMessage: wonRate:" + wonRate);
                        Log.i(TAG, "handleMessage: update_date:" + time);

                        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putFloat("dollar_rate",dollarRate);
                        editor.putFloat("euro_rate",euroRate);
                        editor.putFloat("won_rate",wonRate);
                        editor.putString("update_date",time);
                        editor.apply();

                        Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT);
                    }
                }
            };
        }
    }

    public void onClick(View btn){
        Log.i(TAG,"onClick:");
        String str=rmb.getText().toString();
        Log.i(TAG,"onClick: get str="+str);

        float r=0;
        if(str.length()>0){
            r=Float.parseFloat(str);
        }else {
            Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG,"onClick:r="+ r);

        if(btn.getId()==R.id.dollar){
            show.setText(String.valueOf(r*dollarRate));
        }else if(btn.getId()==R.id.euro){
            show.setText(String.valueOf(r*euroRate));
        }else{
            show.setText(String.valueOf(r*wonRate));
        }
    }

    public void openOne(View btn){
        Intent config=new Intent(this,ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG,"openOne:dollarRate="+dollarRate);
        Log.i(TAG,"openOne:euroRate="+euroRate);
        Log.i(TAG,"openOne:wonRate="+wonRate);

        startActivityForResult(config,1);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1&&resultCode==2){
            Bundle bundle =data.getExtras();
            dollarRate=bundle.getFloat("key_dollar",0.1f);
            euroRate=bundle.getFloat("key_euro",0.1f);
            wonRate=bundle.getFloat("key_won",0.1f);

            SharedPreferences sp=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor =sp.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"onActivityResult:数据已保存到sharedPreferences");

            Log.i(TAG,"onActivityResult:dollarRate"+dollarRate);
            Log.i(TAG,"onActivityResult:euroRate"+euroRate);
            Log.i(TAG,"onActivityResult:wonRate"+wonRate);

        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void run(){
        Log.i(TAG,"run:run()……");
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        //用于保存获取的汇率
        Bundle bundle=new Bundle();

//        //获取Msg对象，用于返回主线程
//        Message msg =handler.obtainMessage(5);
//        msg.obj="Hello from run";
//        handler.sendMessage(msg);

//        //获取网络数据
//        URL url=null;
//        try{
//            url=new URL("http://www.usd-cny.com/icbc.htm");
//            HttpURLConnection http=(HttpURLConnection) url.openConnection();
//            InputStream in=http.getInputStream();
//
//            String html = inputStream2String(in);
//            Log.i(TAG,"run:html="+html);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }catch(IOException e){
//            e.printStackTrace();
//        }

        Document doc =null;
        try{
            String url="http:/www.usd-cny.com/bankofchina.htm";
            doc=Jsoup.connect(url).get();

            //获取当前更新时间
            SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
            String timea=(String )sd.format(new Date());
            Log.i(TAG,"timeaaaaa:"+timea);
            bundle.putString("update-date",timea);

            Log.i(TAG,"run:"+doc.title());
            Elements tables=doc.getElementsByTag("table");
            Element table1= tables.get(0);
            //获取td中的数据
            Elements tds=table1.getElementsByTag("td");
            for(int i=0;i< tds.size();i+=6){
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);

                String str1=td1.text();
                String val=td2.text();
                Log.i(TAG,"run:"+str1+"==>"+val);

                float v= 100f/Float.parseFloat(val);
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",v);
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",v);
                }else if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",v);
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        //Bundle中保存所获取的汇率

        //获取Msg对象，用于返回主线程
        Message msg =handler.obtainMessage(5);
        msg.obj=bundle;
        handler.sendMessage(msg);
    }

    private  String inputStream2String(InputStream inputStream) throws IOException{
        final int bufferSize=1024;
        final char[] buffer =new char[bufferSize];
        final StringBuilder out =new StringBuilder();
        Reader in =new InputStreamReader(inputStream,"gb2312");
        while (true){
            int rsz=in.read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}


