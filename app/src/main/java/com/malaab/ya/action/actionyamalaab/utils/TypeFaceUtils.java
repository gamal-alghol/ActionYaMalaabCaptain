package com.malaab.ya.action.actionyamalaab.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;


public class TypeFaceUtils {

    private static final String TYPEFACE_FOLDER = "fonts";

    private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<>();


    public static Typeface getTypeFace(Context context, String fileName) {
        Typeface tempTypeface = sTypeFaces.get(fileName);

        if (tempTypeface == null) {
            String fontPath = TYPEFACE_FOLDER + '/' + fileName;
            tempTypeface = Typeface.createFromAsset(context.getAssets(), fontPath);
            sTypeFaces.put(fileName, tempTypeface);
        }

        return tempTypeface;
    }
}
