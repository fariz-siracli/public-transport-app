package com.fs.mobile.tansportcatalog.utils;


import android.content.Context;
import com.fs.mobile.tansportcatalog.R;

import java.util.Locale;


/**
 * @author f.siracli
 */
public enum Language {


    AZERBIJAN(1068, "az", new Locale("az", "AZ")),   //,LoginActivity.getContext().getString(R.string.az)),
    RUSSIAN(1049, "ru", new Locale("ru", "RU")), //LoginActivity.getContext().getString(R.string.rus)),
    ENGLISH(1033, "en", new Locale("en", "EN"));//LoginActivity.getContext().getString(R.string.eng));

    private int code;
    private String shortName;
    private Locale locale;

    Language(int code, String shortName, Locale locale) {
        this.code = code;
        this.shortName = shortName;
        this.locale = locale;
    }


    public static String getName(Context context, int ordinal) {
        switch (ordinal) {
            case 0:
                return context.getString(R.string.az);
            case 1:
                return context.getString(R.string.rus);
            case 2:
                return context.getString(R.string.eng);
        }
        return null;
    }


    public static int getCode(Language dt) {
        return (dt == null) ? null : dt.code;

    }

    public static Language lookup(Integer code) {
        if (code == null) {
            return null;
        }
        for (Language dt : values()) {
            if (dt.code == code) {
                return dt;
            }
        }
        return null;
    }

    public static Language lookup(String shrtName) {
        if (shrtName == null) {
            return null;
        }
        for (Language dt : values()) {
            if (dt.shortName.equals(shrtName)) {
                return dt;
            }
        }
        return null;
    }

    public static Language getByLocale(Locale locale) {
        if (locale == null) {
            return null;
        }
        for (Language language : values()) {
            if (language.locale.equals(locale)) {
                return language;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }


    @Override
    public String toString() {
        return shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public Locale getLocale() {
        return locale;
    }
}