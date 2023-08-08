package com.axb.upgrade.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<T extends ViewBinding, P extends IBasePresenter> extends AppCompatActivity {

    public T binding;

    // Fields
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //返回当前类的父类的Type，也就是BaseActivity
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            //如果支持泛型
            try {
                //获得泛型中的实际类型，可能会存在多个泛型，[0]也就是获得T的type
                Class<T> clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
                //反射inflate
                Method method = clazz.getMethod("inflate", LayoutInflater.class);
                //方法调用，获得ViewBinding实例
                binding = (T) method.invoke(null, getLayoutInflater());
            } catch (Exception e) {
                e.printStackTrace();
            }
            setContentView(binding.getRoot());
        }
        mPresenter = getPresenter();
        init();
        readIntentExtras(getIntent(), false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }

    protected abstract void init();
    protected abstract void readIntentExtras(Intent intent, boolean newIntent);
    protected abstract @NonNull P getPresenter();

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
