package com.zhibo8.game.sdk.base;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8CodeInfo {

    public static int CODE_UNINITIALIZED = 1001; //未初始化sdk
    public static int CODE_INSTALL_LATEST_ZHIBO8 = 1002;//未安装
    public static int CODE_AUTHORIZE_FAILURE = 2001; //授权失败
    public static int CODE_CANCEL_AUTHORIZE = 2002; // 取消授权
    public static int CODE_TEENAGER_PROTECT = 3001; //未成年暂时无法体验游戏,拦截用户进入游戏
    public static int CODE_VERIFY_FAILURE = 3002; //认证失败
    public static int CODE_PAY_FAILURE = 4001; //支付失败
    public static int CODE_CANCEL_PAY = 4002; //用户取消支付


    public static String MSG_INSTALL_LATEST_ZHIBO8 = "请安装最新版本直播吧app";
    public static String MSG_VERIFY_FAILURE = "认证失败";
    public static String MSG_UNINITIALIZED = "未初始化sdk";
    public static String MSG_AUTHORIZE_FAILURE = "授权失败";
    public static String MSG_PAY_FAILURE = "支付失败";
    public static String MSG_CODE_TEENAGER_PROTECT = "未成年暂时无法体验游戏,拦截用户进入游戏";
    public static String MSG_CANCEL_AUTHORIZE = "用户取消授权";
    public static String MSG_CANCEL_PAY = "用户取消支付";



}
