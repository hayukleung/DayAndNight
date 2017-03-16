package com.hayukleung.dayandnight;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * DayAndNight
 * com.hayukleung.dayandnight
 * SettingsUtils.java
 *
 * by hayukleung
 * at 2017-03-16 11:01
 */

class SettingsUtils {

  private static final String KEY_THEME = "theme";

  public static final int VAL_THEME_DAY = 1;
  public static final int VAL_THEME_NIGHT = 2;

  private static volatile SettingsUtils mInstance;
  private SharedPreferences mSharedPreferences;

  public static SettingsUtils getInstance(Context context) {
    if (null == mInstance) {
      synchronized (SettingsUtils.class) {
        if (null == mInstance) {
          mInstance = new SettingsUtils(context);
        }
      }
    }
    return mInstance;
  }

  private SettingsUtils(Context context) {
    mSharedPreferences = context.getSharedPreferences("dn.pref", Context.MODE_PRIVATE);
  }

  public SharedPreferences getSharedPreferences() {
    return mSharedPreferences;
  }

  public int getTheme() {
    return mSharedPreferences.getInt(KEY_THEME, VAL_THEME_DAY);
  }

  public void setTheme(int theme) {
    mSharedPreferences.edit().putInt(KEY_THEME, theme).apply();
  }
}
