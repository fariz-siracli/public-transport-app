package com.fs.mobile.tansportcatalog.utils;

/**
 * Created by Admin on 15.10.2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class MyContextWrapper extends ContextWrapper {

    public MyContextWrapper(Context base) {
        super(base);
    }

    @SuppressWarnings("deprecation")
    public static ContextWrapper wrap(Context context) {
        Locale locale = getLang(context).getLocale();
        Configuration config = context.getResources().getConfiguration();

        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(config, locale);
        } else {
            setSystemLocaleLegacy(config, locale);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(config);
        } else {
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
        return new MyContextWrapper(context);
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration config, Locale locale) {
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale) {
        config.setLocale(locale);
    }

    public static Language getLang(Context newBase) {
        Language language = Language.ENGLISH;
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(newBase);
        String defLang = sharedPrefs.getString(Constants.SAVED_USER_LANGUAGE, "-1");
        if (defLang != null && !defLang.equals("-1")) {
            Constants.language = defLang;
            Language appLanguage = Language.lookup(defLang);
            if (appLanguage != null) {
                language = appLanguage;
            }
        } else {
            Locale def = Locale.getDefault();
            if (def.equals(Language.AZERBIJAN.getLocale())) {
                Constants.language = "az";
                language = Language.AZERBIJAN;
            } else if (def.equals(Language.RUSSIAN.getLocale())) {
                Constants.language = "ru";
                language = Language.RUSSIAN;
            } else {
                Constants.language = "en";
                language = Language.ENGLISH;
            }
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(newBase).edit();
            editor.putString(Constants.SAVED_USER_LANGUAGE, Constants.language);
            editor.commit();
        }
        return language;
    }
}
