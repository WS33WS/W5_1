package com.example.shan.week3;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG ="0" ;
    TextView a_score;
    TextView b_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.i(TAG,"onCreate:");

        a_score=(TextView) findViewById(R.id.a_score);
        b_score=(TextView) findViewById(R.id.b_score);
    }

    public void onStart(){
        super.onStart();
        Log.i(TAG,"onStart:");
    }

    public void onResume(){
        super.onResume();
        Log.i(TAG,"onResume:");
    }

    public void onRestart(){
        super.onRestart();
        Log.i(TAG,"onRestart:");
    }

    public void onPause(){
        super.onPause();
        Log.i(TAG,"onPause:");
    }

    public void onStop(){
        super.onStop();
        Log.i(TAG,"onStop:");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"onDestroy:");
    }

    public void A_btnAdd1(View btn){
        A_showScore(0);
    }

    public void A_btnAdd2(View btn){
        A_showScore(2);
    }

    public void A_btnAdd3(View btn){
        A_showScore(3);
    }

    public void B_btnAdd1(View btn){
        B_showScore(0);
    }


    public void B_btnAdd2(View btn){
        B_showScore(2);
    }

    public void B_btnAdd3(View btn){
        B_showScore(3);
    }

    public void btnReset(View btn){
        a_score.setText("0");
        b_score.setText("0");
    }

    private void A_showScore(int inc){
        Log.i("show","inc=" + inc);
        String oldScore = (String) a_score.getText();
        int newScore = Integer.parseInt(oldScore) + inc;
        a_score.setText("" + newScore);
    }

    private void B_showScore(int inc){
        Log.i("show","inc=" + inc);
        String oldScore = (String) b_score.getText();
        int newScore = Integer.parseInt(oldScore) + inc;
        b_score.setText("" + newScore);
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        String  socrea= (String) ((TextView)findViewById(R.id.a_score)).getText();
        String  socreb= (String) ((TextView)findViewById(R.id.b_score)).getText();

        Log.i(TAG,"onSaveInstanceState:");
        outState.putString("teama_score",socrea);
        outState.putString("teamb_score",socreb);
    }

    protected void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        String scorea=saveInstanceState.getString("teama_score");
        String scoreb=saveInstanceState.getString("teamb_score");

        Log.i(TAG,"onRestoreInstanceState:");
        ((TextView)findViewById(R.id.a_score)).setText(scorea);
        ((TextView)findViewById(R.id.b_score)).setText(scoreb);

    }
}
