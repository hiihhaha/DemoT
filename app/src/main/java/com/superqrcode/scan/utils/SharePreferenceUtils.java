package com.superqrcode.scan.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.superqrcode.scan.Const;


public class SharePreferenceUtils {

    public static boolean shouldShowRatePopup(Context context) {
        if (isRated(context)) {
            return false;
        }
        int count = getCountRate(context);
        return count == 0 || count == 1 || count == 2 || count == 3 || count == 5 || count == 7 || count == 10 || count == 13 || count == 18 || count == 24;
    }


    public static boolean isFirstTime(Context context) {
        SharedPreferences pre = context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        boolean b = pre.getBoolean(Const.IS_FIRST, true);
        if (b) {
            pre.edit().putBoolean(Const.IS_FIRST, false).apply();
        }
        return b;
    }

    public static boolean isRated(Context context) {
        SharedPreferences pre = context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        return pre.getBoolean(Const.IS_RATE, false);
    }

    public static void setRated(Context context) {
        SharedPreferences pre = context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        pre.edit().putBoolean(Const.IS_RATE, true).apply();
    }

    public static int getCountRate(Context context) {
        return context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE).getInt(Const.COUNT_RATE, 0);
    }

    public static void increaseCountRate(Context context) {
        context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE).edit().putInt(Const.COUNT_RATE, getCountRate(context) + 1).apply();
    }

    public static void setBeep(Context context, boolean b) {
        SharedPreferences pre = context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        pre.edit().putBoolean("beep", b).apply();
    }

    public static boolean isBeep(Context context) {
        return context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE).getBoolean("beep", true);
    }


    public static void setFocus(Context context, boolean b) {
        SharedPreferences pre = context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        pre.edit().putBoolean("focus", b).apply();
    }

    public static boolean isFocus(Context context) {
        return context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE).getBoolean("focus", true);
    }


    public static void setCopy(Context context, boolean b) {
        SharedPreferences pre = context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        pre.edit().putBoolean("copy", b).apply();
    }

    public static boolean isCopy(Context context) {
        return context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE).getBoolean("copy", false);
    }

    public static void setOpenUrlAuto(Context context, boolean b) {
        SharedPreferences pre = context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        pre.edit().putBoolean("open_auto", b).apply();
    }

    public static boolean isOpenUrlAuto(Context context) {
        return context.getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE).getBoolean("open_auto", false);
    }

}
