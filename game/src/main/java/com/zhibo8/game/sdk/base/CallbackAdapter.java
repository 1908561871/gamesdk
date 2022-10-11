package com.zhibo8.game.sdk.base;

import org.json.JSONObject;

/**
 * @author : ZhangWeiBo
 * date : 2022/10/11
 * email : 1908561871@qq.com
 * description : TODO
 */
public class CallbackAdapter {

   public static class PayCallBackAdapter  implements ZB8PayRequestCallBack{

       @Override
       public void onFailure(int code, String info) {

       }

       @Override
       public void onSuccess() {

       }

       @Override
       public void onCancel() {

       }
   }

   public static class LoginCallbackAdapter implements ZB8LoginRequestCallBack{

       @Override
       public void onFailure(int code, String info) {

       }

       @Override
       public void onSuccess(JSONObject jsonObject) {

       }

       @Override
       public void onCancel() {

       }
   }
}
