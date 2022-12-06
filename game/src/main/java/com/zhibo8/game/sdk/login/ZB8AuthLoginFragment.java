package com.zhibo8.game.sdk.login;

import static com.zhibo8.game.sdk.base.ZB8Constant.BASE_URL;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhibo8.game.sdk.R;
import com.zhibo8.game.sdk.base.BaseDialogFragment;
import com.zhibo8.game.sdk.base.ZB8CodeInfo;
import com.zhibo8.game.sdk.base.ZB8Constant;
import com.zhibo8.game.sdk.base.ZB8LoadingLayout;
import com.zhibo8.game.sdk.base.ZB8LoadingView;
import com.zhibo8.game.sdk.base.ZB8LoginRequestCallBack;
import com.zhibo8.game.sdk.core.ZBGlobalConfig;
import com.zhibo8.game.sdk.net.ZB8OkHttpUtils;
import com.zhibo8.game.sdk.utils.AnimatorUtils;
import com.zhibo8.game.sdk.utils.CommonUtils;
import com.zhibo8.game.sdk.utils.DisplayUtils;
import com.zhibo8.game.sdk.utils.ZB8HtmlUtils;
import com.zhibo8.game.sdk.utils.ZB8LogUtils;
import com.zhibo8.game.sdk.verify.ZBHeartBeatManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/30
 * email : 1908561871@qq.com
 * description : 用户授权
 */
public class ZB8AuthLoginFragment extends BaseDialogFragment implements ZB8LoadingView.OnRetryClickListener {

    private TextView mTvAuthorize;
    private CheckBox mCbAgreement;
    private TextView mTvPrivacy;
    private ZB8SsoHandler zhibo8SsoHandler;
    private ImageView mIvClose;
    private ZB8LoginRequestCallBack callBack;
    private View mLayoutAgreement;
    private PopupWindow mPop;
    private ZB8LoadingLayout mLoadingView;


    public static ZB8AuthLoginFragment getInstance() {
        return new ZB8AuthLoginFragment();
    }

    @Override
    protected void initData(Bundle arguments) {
        if (ZB8LoginManager.getInstance().hasLogin()) {
            getAccessToken(null, ZB8LoginManager.getInstance().getRefreshToken());
        } else {
            getAuthPageInfo();
        }
    }

    @Override
    protected void initView(View view) {
        mTvAuthorize = view.findViewById(R.id.tv_authorize);
        mCbAgreement = view.findViewById(R.id.cb_agreement);
        mTvPrivacy = view.findViewById(R.id.tv_privacy);
        mIvClose = view.findViewById(R.id.iv_close);
        mLayoutAgreement = view.findViewById(R.id.layout_agreement);
        mTvAuthorize.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mLoadingView = view.findViewById(R.id.loading);
        mLoadingView.setOnRetryClickListener(this);

    }

    @Override
    protected int getContentViewId() {
        return R.layout.zb8_dialog_auth_login;
    }


    /**
     * 开始抖动动画
     */
    private void startNope() {
        ObjectAnimator nope = AnimatorUtils.nope(mLayoutAgreement, DisplayUtils.dipToPix(getContext(), 20), 150);
        nope.setRepeatCount(3);
        nope.start();
    }


    protected boolean checkHasAgreement() {
        if (mCbAgreement.isChecked()) {
            return true;
        }
        startNope();
        showAgreementTip();
        return false;
    }


    /**
     * 显示同意用户协议提示语
     */
    private void showAgreementTip() {
        mCbAgreement.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!getActivity().isFinishing() && (mPop == null || !mPop.isShowing())) {
                    mPop = showAgreement(mCbAgreement);
                }
            }
        }, 450);
    }

    ZB8AuthListener authListener = new ZB8AuthListener() {

        @Override
        public void onSuccess(JSONObject jsonObject) {
            getAccessToken(jsonObject.optString("code"), null);
        }

        @Override
        public void cancel() {
            ZB8LogUtils.d("直播吧授权取消");
            callBack.onFailure(ZB8CodeInfo.CODE_CANCEL_AUTHORIZE,ZB8CodeInfo.MSG_CANCEL_AUTHORIZE);
        }

        @Override
        public void onFailure(int errorCode, String errorMsg) {
            ZB8LogUtils.d("直播吧授权失败");
            callBack.onFailure(errorCode, errorMsg);
        }
    };


    private void getAccessToken(String code, String refresh_token) {
        mLoadingView.showLoading();
        String appid = ZBGlobalConfig.getInstance().getConfig().getAppId();
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(code)) {
            map.put("code", code);
        }
        if (!TextUtils.isEmpty(refresh_token)) {
            map.put("refresh_token", refresh_token);
            map.put("grant_type", "refresh_token");
        } else {
            map.put("grant_type", "authorization_code");
        }
        map.put("appid", appid);
        ZB8OkHttpUtils.getInstance().doPost(BASE_URL + "/sdk/m_game_auth/accessToken", map,getRequestTag(), new ZB8OkHttpUtils.OkHttpCallBackListener() {

            @Override
            public void failure(Exception e) throws Exception{
                callBack.onFailure(ZB8CodeInfo.CODE_AUTHORIZE_FAILURE, ZB8CodeInfo.MSG_AUTHORIZE_FAILURE);
                ZB8LogUtils.d("获取token失败:"+e.getMessage());
                showError();
            }

            @Override
            public void success(String json) throws Exception {
                JSONObject jsonObject = new JSONObject(json);
                String msg = jsonObject.optString("msg");

                if (TextUtils.equals(jsonObject.optString("status"), "success")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    if (data != null) {
                        String access_token = data.optString("access_token");
                        String refresh_token = data.optString("refresh_token");
                        getUserInfo(access_token, refresh_token);
                        return;
                    }
                }
                ZB8LoginManager.getInstance().logout();
                ZB8LogUtils.d("获取token失败,重新调用授权登录弹窗");
                callBack.onFailure(ZB8CodeInfo.CODE_FAILURE_TOKEN, !TextUtils.isEmpty(msg) ? msg : ZB8CodeInfo.MSG_FAILURE_TOKEN);
                getAuthPageInfo();
            }
        });
    }

    private void getUserInfo(String access_token, String refresh_token) {
        mLoadingView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("access_token", access_token);
        map.put("appid", ZBGlobalConfig.getInstance().getConfig().getAppId());

        ZB8OkHttpUtils.getInstance().doPost(BASE_URL + "/sdk/m_game/userInfo", map,getRequestTag(), new ZB8OkHttpUtils.OkHttpCallBackListener() {
            @Override
            public void failure(Exception e) throws Exception{
                callBack.onFailure(ZB8CodeInfo.CODE_AUTHORIZE_FAILURE, ZB8CodeInfo.MSG_AUTHORIZE_FAILURE);
                ZB8LogUtils.d("获取用户信息失败");
                showError();
            }

            @Override
            public void success(String json) throws Exception {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject wrapper = new JSONObject();
                String msg = jsonObject.optString("msg");

                if (TextUtils.equals(jsonObject.optString("status"), "success")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    if (data != null) {
                        wrapper.putOpt("refresh_token", refresh_token);
                        wrapper.putOpt("access_token", access_token);
                        wrapper.putOpt("nickname", data.optString("nickname"));
                        wrapper.putOpt("pic", data.optString("pic"));
                        wrapper.putOpt("open_id", data.optString("open_id"));
                        ZB8LogUtils.d("获取用户信息成功："
                                + " token = " + access_token
                                + " nickname = " + data.optString("nickname")
                                + " pic = " + data.optString("pic")
                                + " open_id = " + data.optString("open_id")
                        );
                        checkUserVerify(access_token,wrapper);
                        return;
                    }
                }
                ZB8LogUtils.d("获取用户信息失败："+json);
                callBack.onFailure(ZB8CodeInfo.CODE_AUTHORIZE_FAILURE, !TextUtils.isEmpty(msg) ? msg : ZB8CodeInfo.MSG_AUTHORIZE_FAILURE);
                showError();
            }
        });
    }


    public void setCallBack(ZB8LoginRequestCallBack callBack) {
        this.callBack = callBack;
    }

    public void setZhibo8SsoHandler(ZB8SsoHandler zhibo8SsoHandler) {
        this.zhibo8SsoHandler = zhibo8SsoHandler;
    }

    @Override
    public void onClick(View v) {
        if (v == mTvAuthorize) {
            if (!checkHasAgreement()) {
                startNope();
            } else {
                zhibo8SsoHandler.authorize(authListener);
            }
        } else if (v == mIvClose) {
            callBack.onCancel();
        }
    }


    public PopupWindow showAgreement(View anchor) {
        View pop = LayoutInflater.from(anchor.getContext()).inflate(R.layout.zb8_pop_agreement, null);
        if (anchor.getWindowToken() == null) {
            return null;
        }
        final PopupWindow popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(anchor, -anchor.getWidth() / 8, -DisplayUtils.dipToPix(getActivity(),30) - anchor.getMeasuredHeight());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //最好在使用的生命周期里使用者自己去dismiss,否则这边虽然加了try catch不会应用挂掉,但还是存在一定的泄露问题.
                try {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        }, 5000);
        return popupWindow;
    }


    private void getAuthPageInfo() {
        mLoadingView.showLoading();
        ZB8OkHttpUtils.getInstance().doPost(ZB8Constant.BASE_URL + "/sdk/m_game_auth/page", null,getRequestTag(), new ZB8OkHttpUtils.OkHttpCallBackListener() {
            @Override
            public void failure(Exception e) throws Exception{
                ZB8LogUtils.d("获取授权窗口失败："+e.getMessage());
                mLoadingView.showError();
                callBack.onFailure(ZB8CodeInfo.CODE_AUTHORIZE_FAILURE,ZB8CodeInfo.MSG_AUTHORIZE_FAILURE);
            }

            @Override
            public void success(String json) throws Exception {
                JSONObject result = new JSONObject(json);
                String msg = result.optString("msg");

                if (TextUtils.equals(result.optString("status"), "success")) {
                    JSONObject data = result.optJSONObject("data");
                    if (data != null) {
                        mLoadingView.showContent();
                        String btnText = data.optString("one_key_login_txt");
                        String privacy_policy = data.optString("privacy_policy");
                        String user_agreement = data.optString("user_agreement");
                        mTvAuthorize.setText(btnText);
                        ZB8HtmlUtils.setHtml(getContext().getString(R.string.new_view_agreement_privacy, user_agreement, privacy_policy), mTvPrivacy);
                        return;
                    }
                }
                ZB8LogUtils.d("获取授权窗口失败："+json);
                callBack.onFailure(ZB8CodeInfo.CODE_AUTHORIZE_FAILURE, !TextUtils.isEmpty(msg) ? msg : ZB8CodeInfo.MSG_AUTHORIZE_FAILURE);
                showError();
            }
        });

    }


    /**
     * 检查用户是否认证
     */
    private void checkUserVerify(String token,JSONObject repJson) {
        mLoadingView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("appid", ZBGlobalConfig.getInstance().getConfig().getAppId());
        ZB8OkHttpUtils.getInstance().doPost(BASE_URL + "/sdk/m_game/isAuth", map, getRequestTag(), new ZB8OkHttpUtils.OkHttpCallBackListener() {
            @Override
            public void failure(Exception e) throws Exception {
                ZB8LogUtils.d("获取用户认证信息失败："+e.getMessage());
                callBack.onFailure(ZB8CodeInfo.CODE_VERIFY_FAILURE, ZB8CodeInfo.MSG_VERIFY_FAILURE);
                mLoadingView.showError();
            }

            @Override
            public void success(String json) throws Exception {
                JSONObject jsonObject = new JSONObject(json);
                String msg = jsonObject.optString("msg");

                if (TextUtils.equals(jsonObject.optString("status"), "success")) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    if (data != null) {
                        int is_auth = data.optInt("is_auth");
                        int is_adulth = data.optInt("is_adulth");
                        if (is_auth == 1) {
                            if (is_adulth == 1) {
                                repJson.put("need_verify",false);
                                ZB8LogUtils.d("用户认证成功，且用户已成年");
                                ZBHeartBeatManager.getInstance().setOnlineId(data.optString("online_id"));
                                callBack.onSuccess(repJson);
                            } else {
                                //未成年
                                ZB8LogUtils.d("用户认证成功，用户未成年");
                                callBack.onFailure(ZB8CodeInfo.CODE_TEENAGER_PROTECT, !TextUtils.isEmpty(msg) ? msg : ZB8CodeInfo.MSG_CODE_TEENAGER_PROTECT);
                                CommonUtils.finishActivity(getActivity());
                            }
                        } else {
                            repJson.put("need_verify",true);
                            callBack.onSuccess(repJson);
                        }
                        return;
                    }
                }
                ZB8LogUtils.d("获取用户认证信息失败："+json);
                callBack.onFailure(ZB8CodeInfo.CODE_VERIFY_FAILURE,!TextUtils.isEmpty(msg) ? msg : ZB8CodeInfo.MSG_VERIFY_FAILURE);
                mLoadingView.showError();
            }
        });
    }

    @Override
    public void onRetry() {
        if (ZB8LoginManager.getInstance().hasLogin()) {
            getAccessToken(null, ZB8LoginManager.getInstance().getRefreshToken());
        } else {
            getAuthPageInfo();
        }
    }

    private void showError() {
        if (ZB8LoginManager.getInstance().hasLogin()) {
            mLoadingView.showError();
        } else {
            mLoadingView.showContent();
        }
    }

}
