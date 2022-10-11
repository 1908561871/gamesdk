package com.zhibo8.game.sdk.base;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : 支付回调接口
 */
public interface ZB8PayRequestCallBack {

    void onFailure(int code,String info);

    void onSuccess();

    void onCancel();
}
