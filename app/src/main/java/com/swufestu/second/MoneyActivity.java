package com.swufestu.second;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.lang.annotation.Documented;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//汇率转换主页面
public class MoneyActivity<TAG> extends AppCompatActivity  implements Runnable{

    private static final String TAG = "MoneyActivity";

    EditText rmb; //outputmoney
    TextView show; //output
    Button dollar; //button1
    Button euro; //button2
    Button won; //button3
    double dollarRate = 0.35;
    double euroRate = 0.25;
    double wonRate = 510;
    double money;
    double money1;
    Handler handler; //线程同步，用于接收和发送。注：这里不能有方法可以附初值如null

    public MoneyActivity() throws IOException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        rmb = (EditText) findViewById(R.id.inputrmb);
        show = (TextView) findViewById(R.id.exchange);

        //读取文件中保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollarRate = (double) sharedPreferences.getFloat("new_dollar", 0.0f);
        wonRate = (double) sharedPreferences.getFloat("new_won", 0.0f);
        euroRate = (double) sharedPreferences.getFloat("new_euro", 0.0f);

        //线程同步
        handler = new Handler(Looper.myLooper()) {
            @Override
            //接收消息
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: 收到消息");
                if (msg.what == 6) {
                    Bundle bdl = (Bundle)msg.obj;
                    dollarRate = bdl.getFloat("key_dollar");
                    euroRate = bdl.getFloat("key_euro");
                    wonRate = bdl.getFloat("key_won");
                    Toast.makeText(MoneyActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
//                    String str = (String) msg.obj; //将收到的消息转换成String
//                    Log.i(TAG, "handleMessage: handleMessage:getMessange msg=" + str);
                }
                super.handleMessage(msg);
            }
        };

        //启动线程。执行run()方法
        Thread thread = new Thread(this);
        Log.i(TAG, "onCreate: ");
        thread.start(); //this.run()
    }

    public void click(View btn) {
        //获取用户输入
        EditText rmb = findViewById(R.id.inputrmb);
        TextView result = findViewById(R.id.exchange);
        String culmoney = rmb.getText().toString();
        if(culmoney.length()>0) {
            double inp;
            inp = Double.parseDouble(culmoney);
            double re = 0;
            if (btn.getId() == R.id.dollar) {
                re = inp * dollarRate;
            } else if (btn.getId() == R.id.euro) {
                re = inp * euroRate;
            } else if (btn.getId() == R.id.won) {
                re = inp * wonRate;
            }
            //显示结果
            result.setText(String.valueOf(re));
        }else{
            Toast.makeText(this, "请输入人民币金额", Toast.LENGTH_SHORT).show();
        }
    }

    //页面跳转，打开新的页面
    public void open(View btn) {
                openConfig();
            }

    private void openConfig() {
        Intent config = new Intent(this, MoneyActivity2.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("won_rate_key", wonRate); //poundRate
        config.putExtra("euro_rate_key", euroRate);

        startActivityForResult(config, 1);
    }

    //从2界面直接传数据过来
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == 1) {//直接从2界面传值过来
            dollarRate = data.getDoubleExtra("dollar_key", 0.0);
            euroRate = data.getDoubleExtra("euro_key", 0.0);
            wonRate = data.getDoubleExtra("won_key", 0.0);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    //创建菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    //调用菜单项打开页面
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_setting) {
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }

    //开启线程的run
    @Override
    public void run() {
        //延迟
        try {
            Thread.sleep(3000); //休息3秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run: run()......");

        //获取网络数据
        URL url = null;
        try {
            url = new URL("https://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in); //将输入流转化为字符串
            Log.i(TAG, "run: html=" + html);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //获取Msg对象，用于返回主线程
        //发送消息给主线程
        Message msg = handler.obtainMessage(6);
        //msg.what = 6; /以上两种方法都可以
        msg.obj = "Hello from run";
        handler.sendMessage(msg);
        Log.i(TAG, "run: 消息已发送");


        //Jsoup
        //获取标题，从一个汇率网站，并做到每天更新的关键代码
        Bundle bundle = new Bundle(); //这里一定要加！！！
        try {
            Document doc = Jsoup.connect("https://usd-cny.com/").get();
            Log.i(TAG,"run:title="+ doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element firstTable = doc.getElementsByTag("table").first();//从集合中直接获得，避免标签不同产生问题

            Elements trs = firstTable.getElementsByTag("tr");
            trs.remove(0);//或者直接去除第一行
            for(Element tr : trs){
                Log.i(TAG,"run: tr=" + trs);
                //从行中获取td元素
                Elements tds = firstTable.getElementsByTag("td");
                //Log.i(TAG,"run:tds.count=" + tds.size());
                String td1 = tds.get(0).text();
                String td2 = tds.get(4).text();
                //运行出币种+数据
                Log.i(TAG,"run:td1=" + td1 + "--->" + td2);
                if("美元".equals(td1)) {
                    bundle.putFloat("key dollar", 100/Float.parseFloat(td2));
//                    String val = td2;
//                    dollarRate=100f/Float.parseFloat(val);
//                    Log.i(TAG, "run: 美元汇率"+dollarRate);
                }else if("欧元".equals(td1)){
                    bundle.putFloat("key euro",100/Float.parseFloat(td2));
//                    String val = td2;
//                    euroRate=100f/Float.parseFloat(val);
//                    Log.i(TAG, "run: 欧元汇率"+euroRate);
                }else if("韩币".equals(td1)){
                    bundle.putFloat("key won",100/Float.parseFloat(td2));
//                    String val = td2;
//                    wonRate=100f/Float.parseFloat(val);
//                    Log.i(TAG, "run: 韩币汇率"+wonRate);
                }
            }
        }catch (IOException e){
            e.printStackTrace();;
        }

    }

    //获取界面内容
    private String inputStream2String(InputStream inputStream)
            throws IOException {
        final int buffersize = 1024;
        final char[] buffer = new char[buffersize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);

        }
        return out.toString();

    }


}

