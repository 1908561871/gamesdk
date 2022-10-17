package com.zhibo8.game.sdk.verify;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.zhibo8.game.sdk.base.ZB8Constant;
import com.zhibo8.game.sdk.core.ZBGlobalConfig;
import com.zhibo8.game.sdk.login.ZB8LoginManager;
import com.zhibo8.game.sdk.net.ZB8OkHttpUtils;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author : ZhangWeiBo
 * date : 2022/10/14
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZBHeartBeatManager implements LifecycleObserver {

    public static int DEFAULT_HEART_DURING = 30000; //30秒
    private int during = DEFAULT_HEART_DURING;

    public static ZBHeartBeatManager instance = new ZBHeartBeatManager();

    private String mOnlineId;

    private Handler handler = new Handler(Looper.getMainLooper());

    private boolean isStartBefore = false;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            upload();
            handler.postDelayed(this,during);
        }
    };

    public static ZBHeartBeatManager getInstance() {
        return instance;
    }

    private ZBHeartBeatManager() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void setOnlineId(String mOnlineId) {
        this.mOnlineId = mOnlineId;
    }

    public void start() {
        isStartBefore = true;
        if (!TextUtils.isEmpty(mOnlineId)){
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable,during);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (isStartBefore){
            start();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPaused() {
        stop();
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public void clear(){
        mOnlineId = null;
        isStartBefore = false;
        stop();
    }

    private void upload(){
        Map<String, String> map = new HashMap<>();
        map.put("access_token", ZB8LoginManager.getInstance().getToken());
        map.put("appid", ZBGlobalConfig.getInstance().getConfig().getAppId());
        map.put("online_id",mOnlineId);
        ZB8OkHttpUtils.getInstance().doPost(ZB8Constant.BASE_URL + "/sdk/m_game/online", map, null, new ZB8OkHttpUtils.OkHttpCallBackListener() {
            @Override
            public void failure(Exception e) throws Exception {

            }

            @Override
            public void success(String json) throws Exception {

            }
        });
    }

}
