package com.swufestu.second;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class BallActivity extends AppCompatActivity{

    private static final String TAG = "BallActivity";

    TextView pointA;
    TextView pointB;
    Button point3a;
    Button point2a;
    Button point1a;
    Button point3b;
    Button point2b;
    Button point1b;
    Button reset;
    int scoreA = 0;
    int scoreB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_ball2);

        Button btn = findViewById(R.id.click);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View btn){
                //Log.i(TAG, msg:"click: ");
                if(btn.getId()==R.id.point3a){
                    scoreA += 3;
                }else if(btn.getId()==R.id.point2a){
                    scoreA += 2;
                }else if(btn.getId()==R.id.point1a){
                    scoreA += 1;
                }else{
                    //reset
                    scoreA = 0;
                }
                showScore();
            }
            private void showScore(){
                TextView showA = findViewById((R.id.pointA));
                showA.setText(String.valueOf(pointA));
                TextView showB = findViewById((R.id.pointB));
                showB.setText(String.valueOf(pointB));
            }

            public  void onClickb(View btn){
                //Log.i(TAG, msg:"click: ");
                if(btn.getId()==R.id.point3b){
                    scoreB += 3;
                }else if(btn.getId()==R.id.point2b){
                    scoreB += 2;
                }else if(btn.getId()==R.id.point1b){
                    scoreB += 1;
                }else{
                    //reset
                    scoreB = 0;
                }
                showScore();
            }
        });
        //btn.setOnClickListener(this);
        //this.onClick

    }
}