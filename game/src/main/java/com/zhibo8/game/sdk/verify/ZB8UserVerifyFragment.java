package com.zhibo8.game.sdk.verify;

import static com.zhibo8.game.sdk.base.ZB8Constant.BASE_URL;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhibo8.game.sdk.R;
import com.zhibo8.game.sdk.base.BaseDialogFragment;
import com.zhibo8.game.sdk.base.ZB8CodeInfo;
import com.zhibo8.game.sdk.base.ZB8LoadingLayout;
import com.zhibo8.game.sdk.base.ZB8LoadingView;
import com.zhibo8.game.sdk.base.ZB8LoginRequestCallBack;
import com.zhibo8.game.sdk.core.ZBGlobalConfig;
import com.zhibo8.game.sdk.net.ZB8OkHttpUtils;
import com.zhibo8.game.sdk.utils.CommonUtils;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/30
 * email : 1908561871@qq.com
 * description : 用户认证
 */
public class ZB8UserVerifyFragment extends BaseDialogFragment implements TextWatcher, ZB8LoadingView.OnRetryClickListener {

    private String mToken;
    private EditText mEtUserIdentify;
    private EditText mEtUserName;
    private TextView mTvSubmit;

    private ZB8LoginRequestCallBack callBack;
    private ImageView mIvClose;
    private ZB8LoadingLayout mLoadingView;
    private JSONObject mJsonEntity;

    public static ZB8UserVerifyFragment getInstance(JSONObject jsonObject) {
        ZB8UserVerifyFragment fragment = new ZB8UserVerifyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("auth_info", jsonObject.toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData(Bundle arguments) {
        String auth_info = arguments.getString("auth_info");
        try {
            this.mJsonEntity = new JSONObject(auth_info);
            this.mToken = mJsonEntity.optString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView(View view) {
        mEtUserIdentify = view.findViewById(R.id.et_user_identify);
        mEtUserName = view.findViewById(R.id.et_user_name);
        mTvSubmit = view.findViewById(R.id.tv_submit);
        mIvClose = view.findViewById(R.id.iv_close);
        mEtUserName.addTextChangedListener(this);
        mEtUserIdentify.addTextChangedListener(this);
        mLoadingView = view.findViewById(R.id.loading);
        mTvSubmit.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mLoadingView.setOnRetryClickListener(this);
        mLoadingView.showContent();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.zb8_dialog_user_verify;
    }


    @Override
    public void onClick(View v) {
        if (v == mTvSubmit) {
            submitInfo(mEtUserIdentify.getText().toString(), mEtUserName.getText().toString(), mToken);
        } else if (v == mIvClose) {
            callBack.onCancel();
        }
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTvSubmit.setEnabled(!TextUtils.isEmpty(mEtUserIdentify.getText()) && !TextUtils.isEmpty(mEtUserName.getText()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private void submitInfo(String identify, String userName, String access_token) {
        mLoadingView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("access_token", access_token);
        map.put("identify", identify);
        map.put("real_name", userName);
        map.put("appid", ZBGlobalConfig.getInstance().getConfig().getAppId());
        ZB8OkHttpUtils.getInstance().doPost(BASE_URL + "/sdk/m_game/auth", map, getRequestTag(), new ZB8OkHttpUtils.OkHttpCallBackListener() {
            @Override
            public void failure(Exception e) throws Exception {
                ZB8LogUtils.d("提交认证失败："+e.getMessage());
                mLoadingView.showContent();
                callBack.onFailure(ZB8CodeInfo.CODE_VERIFY_FAILURE, ZB8CodeInfo.MSG_VERIFY_FAILURE);
            }

            @Override
            public void success(String json) throws Exception {
                JSONObject jsonObject = new JSONObject(json);
                String msg = jsonObject.optString("msg");
                if (TextUtils.equals(jsonObject.optString("status"), "success")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    int is_adulth = data.optInt("is_adulth");
                    if (is_adulth == 1) {
                        ZB8LogUtils.d("用户认证成功，且用户已成年");
                        callBack.onSuccess(mJsonEntity);
                    } else {
                        //未成年
                        ZB8LogUtils.d("用户认证成功，用户未成年");
                        callBack.onFailure(ZB8CodeInfo.CODE_TEENAGER_PROTECT, !TextUtils.isEmpty(msg) ? msg : ZB8CodeInfo.MSG_CODE_TEENAGER_PROTECT);
                        CommonUtils.finishActivity(getActivity());
                    }
                } else {
                    ZB8LogUtils.d("提交认证失败："+json);
                    mLoadingView.showContent();
                    callBack.onFailure(ZB8CodeInfo.CODE_VERIFY_FAILURE,TextUtils.isEmpty(msg)? ZB8CodeInfo.MSG_VERIFY_FAILURE:msg);
                }
            }
        });
    }


    public void setCallBack(ZB8LoginRequestCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onRetry() {
    }
}
