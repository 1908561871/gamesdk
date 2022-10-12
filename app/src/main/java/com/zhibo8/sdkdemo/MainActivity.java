package com.zhibo8.sdkdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zhibo8.game.sdk.base.ZB8LoginRequestCallBack;
import com.zhibo8.game.sdk.base.ZB8PayRequestCallBack;
import com.zhibo8.game.sdk.core.ZB8Game;

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

    int i = 240;
    private void pay() {

        ZB8Game.pay(MainActivity.this, "GM202210101665389978" + (i++), "1", new ZB8PayRequestCallBack() {
            @Override
            public void onFailure(int code, String info) {
                Toast.makeText(MainActivity.this,info,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void logout() {
        ZB8Game.logout();
        Toast.makeText(this,"退出登录",Toast.LENGTH_SHORT).show();
    }

    private void login() {

        ZB8Game.login(MainActivity.this, new ZB8LoginRequestCallBack() {
            @Override
            public void onFailure(int code, String info) {
                Toast.makeText(MainActivity.this,info,Toast.LENGTH_SHORT).show();
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