package com.swufestu.second;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    TextView point;
    EditText input1,input2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylayout);

        Button btn = findViewById(R.id.click);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                //获取用户输入
                EditText input1 = findViewById(R.id.input1);
                EditText input2 = findViewById(R.id.input2);
                String height = input1.getText().toString();
                String weight = input2.getText().toString();

                double h,w,BMI;
                String adv;
                h=Double.parseDouble(height);
                w=Double.parseDouble((weight));
                double b = w/(h*h);
                BMI = Math.round(b*100)/100;
                DecimalFormat df = new DecimalFormat("######0.00");

                TextView output = findViewById(R.id.output);
                if(BMI<18.5)
                    adv = "您的体型偏瘦，请增加营养摄入，加强锻炼,注意身体健康！";
                else if(BMI<23.9)
                    adv = "您的体型正常，请继续保持！";
                else if(BMI<27.9)
                    adv = "您的体型偏胖，请改变不健康的生活习惯，加强锻炼！";
                else
                    adv = "您的体型肥胖，相关疾病风险显著增加，请注意您的身体健康！";

                output.setText("您的BMI指数为："+String.valueOf(df.format(BMI))+'\n'+adv);
            }
        });
        //btn.setOnClickListener(this);
        //this.onClick

    }
}