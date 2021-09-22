package com.swufestu.second;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

//汇率修改页面
public class MoneyActivity2 extends AppCompatActivity {

    EditText dollarText;
    EditText euroText;
    EditText wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money2);
        Intent intent = getIntent();
        double dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);
        double euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        double won2 = intent.getFloatExtra("won_rate_key",0.0f);

        dollarText=findViewById(R.id.dollarinp);
        euroText=findViewById(R.id.euroinp);
        wonText=findViewById(R.id.woninp);

        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        euroText.setText(String.valueOf(won2));

    }

    public  void clicksave(View btn){
        //重新获取新的汇率数据
        dollarText = findViewById(R.id.dollarinp);
        euroText = findViewById(R.id.euroinp);
        wonText = findViewById(R.id.woninp);
        double newdollar=Double.parseDouble(dollarText.getText().toString());
        double neweuro=Double.parseDouble(euroText.getText().toString());
        double newwon=Double.parseDouble(wonText.getText().toString());

        //在save时保存数据到文件中
        SharedPreferences sp=getSharedPreferences("myrate", Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor= sp.edit();
        editor.putFloat("new_dollar",(float) newdollar);
        editor.putFloat("new_euro",(float) neweuro);
        editor.putFloat("new_won",(float) newwon);
        editor.apply();

        //setResult(1,first);
        finish();//退回到主界面
    }

}