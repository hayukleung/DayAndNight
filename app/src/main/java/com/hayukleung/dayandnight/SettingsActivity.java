package com.hayukleung.dayandnight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

/**
 * DayAndNight
 * com.hayukleung.dayandnight
 * SettingsActivity.java
 *
 * by hayukleung
 * at 2017-03-16 10:46
 */

public class SettingsActivity extends AppCompatActivity {

  private FrameLayout mFL;
  private CheckBox mCBJS;
  private CheckBox mCBZH;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    // TranslucentStatusCompat.requestTranslucentStatus(this);
    // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

    applyTheme();

    setContentView(R.layout.layout_settings);
    // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title_bar);

    mFL = (FrameLayout) findViewById(R.id.fl);
    mCBJS = (CheckBox) findViewById(R.id.cb_js);
    mCBZH = (CheckBox) findViewById(R.id.cb_zh);

    mCBJS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        changeThemeByJS();
      }
    });

    mCBZH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        changeThemeByZH();
      }
    });
  }

  private void applyTheme() {
    SettingsUtils settingsUtils = SettingsUtils.getInstance(this);
    switch (settingsUtils.getTheme()) {
      case SettingsUtils.VAL_THEME_DAY: {
        setTheme(R.style.DayTheme);
        break;
      }
      case SettingsUtils.VAL_THEME_NIGHT: {
        setTheme(R.style.NightTheme);
        break;
      }
      default: {
        break;
      }
    }
  }

  private void toggleTheme() {
    SettingsUtils settingsUtils = SettingsUtils.getInstance(this);
    switch (settingsUtils.getTheme()) {
      case SettingsUtils.VAL_THEME_DAY: {
        setTheme(R.style.NightTheme);
        settingsUtils.setTheme(SettingsUtils.VAL_THEME_NIGHT);
        break;
      }
      case SettingsUtils.VAL_THEME_NIGHT: {
        setTheme(R.style.DayTheme);
        settingsUtils.setTheme(SettingsUtils.VAL_THEME_DAY);
        break;
      }
      default: {
        break;
      }
    }
  }

  /**
   * 使用简书的实现套路来切换夜间主题
   */
  private void changeThemeByJS() {
    toggleTheme();
    refreshUI();
  }

  /**
   * 使用知乎的实现套路来切换夜间主题
   */
  private void changeThemeByZH() {
    showAnimation();
    toggleTheme();
    refreshUI();
  }

  /**
   * 刷新UI界面
   */
  private void refreshUI() {
    TypedValue background = new TypedValue(); // 背景色
    TypedValue textColor = new TypedValue(); // 字体颜色

    Resources.Theme theme = getTheme();
    theme.resolveAttribute(R.attr.dnBackground, background, true);
    theme.resolveAttribute(R.attr.dnTextColor, textColor, true);

    mFL.setBackgroundResource(background.resourceId);

    Resources resources = getResources();
    mCBJS.setTextColor(resources.getColor(textColor.resourceId));
    mCBZH.setTextColor(resources.getColor(textColor.resourceId));

    refreshStatusBar();
  }

  /**
   * 刷新 StatusBar
   */
  private void refreshStatusBar() {
    if (Build.VERSION.SDK_INT >= 21) {
      TypedValue typedValue = new TypedValue();
      Resources.Theme theme = getTheme();
      theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
      getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
    }
  }

  /**
   * 展示一个切换动画
   */
  private void showAnimation() {
    final View decorView = getWindow().getDecorView();
    Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
    if (decorView instanceof ViewGroup && cacheBitmap != null) {
      final View view = new View(this);
      view.setBackgroundDrawable(new BitmapDrawable(getResources(), cacheBitmap));
      ViewGroup.LayoutParams layoutParam =
          new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT);
      ((ViewGroup) decorView).addView(view, layoutParam);
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
      objectAnimator.setDuration(300);
      objectAnimator.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationEnd(Animator animation) {
          super.onAnimationEnd(animation);
          ((ViewGroup) decorView).removeView(view);
        }
      });
      objectAnimator.start();
    }
  }

  /**
   * 获取一个 View 的缓存视图
   *
   * @param view
   * @return
   */
  private Bitmap getCacheBitmapFromView(View view) {
    final boolean drawingCacheEnabled = true;
    view.setDrawingCacheEnabled(drawingCacheEnabled);
    view.buildDrawingCache(drawingCacheEnabled);
    final Bitmap drawingCache = view.getDrawingCache();
    Bitmap bitmap;
    if (drawingCache != null) {
      bitmap = Bitmap.createBitmap(drawingCache);
      view.setDrawingCacheEnabled(false);
    } else {
      bitmap = null;
    }
    return bitmap;
  }
}
