package com.zhibo8.game.sdk.core;

import android.content.Context;
import android.text.TextUtils;

import com.zhibo8.game.sdk.base.ZB8CodeInfo;
import com.zhibo8.game.sdk.base.ZB8Constant;
import com.zhibo8.game.sdk.base.ZB8ContainerActivity;
import com.zhibo8.game.sdk.base.ZB8LoginProxyRequestCallBack;
import com.zhibo8.game.sdk.base.ZB8LoginRequestCallBack;
import com.zhibo8.game.sdk.base.ZB8PayRequestCallBack;
import com.zhibo8.game.sdk.bean.ZBOrderInfo;
import com.zhibo8.game.sdk.login.ZB8LoginManager;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;
import com.zhibo8.game.sdk.utils.ZB8SPUtils;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8Game {

    private static boolean isInit;
    private static Context application;


    private static void init(Context context, Config config) {
        if (!isInit) {
            ZB8Game.isInit = true;
            ZBGlobalConfig.getInstance().setConfig(config);
            application = context.getApplicationContext();
            ZB8SPUtils.init(context);
            ZB8LogUtils.isDebug(config.isDebug());
        }
    }



    public static Config builder() {
        return new Config();
    }


    public static void login(Context context, ZB8LoginRequestCallBack callBack) {
        if (!isInit) {
            callBack.onFailure(ZB8CodeInfo.CODE_UNINITIALIZED, ZB8CodeInfo.MSG_UNINITIALIZED);
            return;
        }
        ZBGlobalConfig.getInstance().setLoginRequestCallBack(new ZB8LoginProxyRequestCallBack(callBack));
        ZB8ContainerActivity.open(context, ZB8Constant.TYPE_AUTHOR);
    }

    public static void logout() {
        ZB8LoginManager.getInstance().logout();
    }


    public static void pay(Context context, String order, String price, ZB8PayRequestCallBack callBack) {
        if (!isInit) {
            callBack.onFailure(ZB8CodeInfo.CODE_UNINITIALIZED, ZB8CodeInfo.MSG_UNINITIALIZED);
            return;
        }
        ZBGlobalConfig.getInstance().setPayRequestCallBack(callBack);
        ZBOrderInfo orderInfo = new ZBOrderInfo();
        orderInfo.setOrder(order);
        orderInfo.setPrice(price);
        ZB8ContainerActivity.open(context, ZB8Constant.TYPE_PAY, orderInfo);
    }


    public static Context getApplication() {
        return application;
    }

    public static class Config {
        String appId;
        String serverKey;
        boolean debug;

        public Config setAppId(String appId) {
            this.appId = appId;
            return this;
        }


        public Config setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public String getServerKey() {
            return serverKey;
        }

        public Config setServerKey(String serverKey) {
            this.serverKey = serverKey;
            return this;
        }

        public String getAppId() {
            return appId;
        }


        public boolean isDebug() {
            return debug;
        }

        public void init(Context context) {

            if (TextUtils.isEmpty(appId)){
                throw new  IllegalArgumentException("appid 不能为空");
            }
            if (TextUtils.isEmpty(serverKey)){
                throw new  IllegalArgumentException("serverKey 不能为空");
            }
            ZB8Game.init(context, this);
        }

    }

}
