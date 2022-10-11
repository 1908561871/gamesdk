package com.zhibo8.game.sdk.core;

import com.zhibo8.game.sdk.base.ZB8LoginRequestCallBack;
import com.zhibo8.game.sdk.base.ZB8PayRequestCallBack;

/**
 * @author : ZhangWeiBo
 * date : 2022/10/11
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZBGlobalConfig {

    private static ZBGlobalConfig instance =  new ZBGlobalConfig();

    private ZB8Game.Config config;

    private ZB8LoginRequestCallBack loginRequestCallBack;
    private ZB8PayRequestCallBack payRequestCallBack;

    public static ZBGlobalConfig getInstance() {
        return instance;
    }

    public ZB8Game.Config getConfig() {
        return config;
    }

    public void setConfig(ZB8Game.Config config) {
        this.config = config;
    }

    public ZB8LoginRequestCallBack getLoginRequestCallBack() {
        return loginRequestCallBack;
    }

    public void setLoginRequestCallBack(ZB8LoginRequestCallBack loginRequestCallBack) {
        this.loginRequestCallBack = loginRequestCallBack;
    }

    public ZB8PayRequestCallBack getPayRequestCallBack() {
        return payRequestCallBack;
    }

    public void setPayRequestCallBack(ZB8PayRequestCallBack payRequestCallBack) {
        this.payRequestCallBack = payRequestCallBack;
    }
}
