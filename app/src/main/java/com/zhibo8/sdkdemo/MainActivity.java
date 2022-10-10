package com.zhibo8.sdkdemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zhibo8.game.sdk.ZB8Game;
import com.zhibo8.game.sdk.ZB8RequestCallBack;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.pay).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.pay:
                pay();
                break;
        }
    }

    private void pay() {
        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZB8Game.pay(MainActivity.this, "1", "1", new ZB8RequestCallBack() {
                    @Override
                    public void onFailure(int code, String info) {

                    }

                    @Override
                    public void onSuccess(JSONObject jsonObject) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    private void logout() {
        ZB8Game.logout();
    }

    private void login() {
        ZB8Game.login(MainActivity.this, new ZB8RequestCallBack() {
            @Override
            public void onFailure(int code, String info) {

            }

            @Override
            public void onSuccess(JSONObject jsonObject) {

                String access_token = jsonObject.optString("access_token");
                String nickname = jsonObject.optString("nickname");
                String pic = jsonObject.optString("pic");
                String open_id = jsonObject.optString("open_id");

                //登陆成功，游戏方其他业务操作 ..
            }

            @Override
            public void onCancel() {

            }
        });
    }
}