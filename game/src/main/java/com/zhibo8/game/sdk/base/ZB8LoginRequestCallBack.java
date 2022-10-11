package com.zhibo8.game.sdk.base;

import org.json.JSONObject;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : 登录回调接口
 */
public interface ZB8LoginRequestCallBack {

    void onFailure(int code,String info);

    void onSuccess(JSONObject jsonObject);

    void onCancel();
}
