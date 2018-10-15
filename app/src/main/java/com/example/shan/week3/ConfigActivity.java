package com.example.shan.week3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {

    private final String TAG="ConfigActivity";

    EditText dollarText;
    EditText euroText;
    EditText wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent=getIntent();
        float dollar2=intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2=intent.getFloatExtra("euro_rate_key",0.0f);
        float won2=intent.getFloatExtra("won_rate_key",0.0f);

        Log.i(TAG,"onCreat:dollar2="+dollar2);
        Log.i(TAG,"onCreat:euro2="+euro2);
        Log.i(TAG,"onCreat:won2="+won2);

        dollarText=(EditText)findViewById(R.id.dollar_rate);
        euroText=(EditText)findViewById(R.id.euro_rate);
        wonText=(EditText)findViewById(R.id.won_rate);

        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));

    }

    public void save(View btn){
        Log.i(TAG, "save:");
        float newDollar=Float.parseFloat(dollarText.getText().toString());
        float newEuro=Float.parseFloat(euroText.getText().toString());
        float newWon=Float.parseFloat(wonText.getText().toString());

        Log.i(TAG,"save:获取到新的值");
        Log.i(TAG,"save:newDollar="+newDollar);
        Log.i(TAG,"save:newEuro="+newEuro);
        Log.i(TAG,"save:newWon="+newWon);

        Intent intent =getIntent();
        Bundle bd1=new Bundle();
        bd1.putFloat("key_dollar",newDollar);
        bd1.putFloat("key_euro",newEuro);
        bd1.putFloat("key_won",newWon);
        intent.putExtras(bd1);
        setResult(2,intent);

        finish();

    }



}
