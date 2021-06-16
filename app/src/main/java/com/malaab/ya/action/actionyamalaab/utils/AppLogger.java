package com.malaab.ya.action.actionyamalaab.utils;

import com.malaab.ya.action.actionyamalaab.BuildConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;


public class AppLogger {

    public static void init() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    public static void d(String s, Object... objects) {
        Logger.d(s, objects);
    }

    public static void i(String s, Object... objects) {
        Logger.i(s, objects);
    }

    public static void w(String s, Object... objects) {
        Logger.w(s, objects);
    }

    public static void e(String s, Object... objects) {
        Logger.e(s, objects);
    }

    public static void json(JSONObject jsonObject) {
        Logger.json(jsonObject.toString());
    }

}
