package com.harry.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HelloAndroidActivity extends Activity {

    Button button;

    Handler myHandler;

    private final static String URL = "http://myi.vip.com/monitor";

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.getData().getString("msg");

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        };

        button = (Button) findViewById(R.id.monitor);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet get = new HttpGet(URL);
                        try {
                            HttpResponse response = httpClient.execute(get);
                            String result = EntityUtils.toString(response.getEntity());
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", result);
                            msg.setData(bundle);
                            myHandler.sendMessage(msg);
                        } catch (Exception e) {

                        }
                    }
                }).start();


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingActivity.class), 0);
                break;
            case R.id.press:
                break;
        }
        return true;
    }

}

