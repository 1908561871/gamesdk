package com.zhibo8.game.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author : ZhangWeiBo
 * date : 2022/10/08
 * email : 1908561871@qq.com
 * description : TODO
 */
public class CommonUtils {

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id="+packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(goToMarket);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void finishActivity(Activity activity){
        if (activity != null){
            activity.finish();
        }
    }

}
