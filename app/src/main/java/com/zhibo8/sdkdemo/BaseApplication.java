package com.zhibo8.sdkdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhibo8.game.sdk.core.ZB8Game;

/**
 * @author : ZhangWeiBo
 * date : 2022/10/10
 * email : 1908561871@qq.com
 * description : TODO
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        ZB8Game.builder()
                .setAppId("zqxzc1d2ez")
                .setAppSecret("test123123")
                .setDebug(true)
                .init(this);

    }
}
