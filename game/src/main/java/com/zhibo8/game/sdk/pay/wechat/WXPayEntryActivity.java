package com.zhibo8.game.sdk.pay.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = "MicroMsg.WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "",false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        WXPayManager.OnWXPayCallBackListener onWXPayCallBackListener = WXPayManager.getInstance().getOnWXPayCallBackListener();
        if (onWXPayCallBackListener == null) {
            return;
        }
        if (resp.errCode == 0) {
            onWXPayCallBackListener.onSuccess();
        } else if (resp.errCode == -2) {
            onWXPayCallBackListener.onCancel();
        } else {
            onWXPayCallBackListener.onFailure();
        }

        ZB8LogUtils.d("微信支付回调： " + " code = " + resp.errCode + "msg = " + resp.errStr);
        finish();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);
    }
}