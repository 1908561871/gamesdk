package com.zhibo8.game.sdk.base;

import com.zhibo8.game.sdk.login.ZB8LoginManager;
import com.zhibo8.game.sdk.verify.ZBHeartBeatManager;

import org.json.JSONObject;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8LoginProxyRequestCallBack implements ZB8LoginRequestCallBack {
    private ZB8LoginRequestCallBack callBack;

    public ZB8LoginProxyRequestCallBack(ZB8LoginRequestCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onFailure(int code, String info) {
        callBack.onFailure(code, info);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            String access_token = jsonObject.optString("access_token");
            String refresh_token = jsonObject.optString("refresh_token");
            ZB8LoginManager.getInstance().login(access_token, refresh_token);
            ZBHeartBeatManager.getInstance().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        callBack.onSuccess(jsonObject);
    }

    @Override
    public void onCancel() {
        callBack.onCancel();
    }
}
